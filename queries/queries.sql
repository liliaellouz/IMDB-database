--MILESTONE 2

--query a
select c.title, rt.runtime 
from runs_for rf, runningTimes rt, clips c, countries cn 
where c.CLIPIDS=rf.clipid and rf.rid=rt.rid and rf.countid=cn.countid 
and cn.COUNTRYNAME='France' 
order by rt.runtime desc
FETCH FIRST 10 ROWS ONLY;

--query b
select cn.countryname, count(r.clipid) as number_of_clips
from released_in r, releasedates rd, countries cn
where r.did=rd.DID and rd.RELEASEDATE='2001' and cn.COUNTID=r.COUNTID
group by cn.countryname;

--query c
select g.genre, count(hg.clipid) as number_of_clips
from clip_genre g, has_genre hg, released_in r, RELEASEDATES rd, countries cn
where hg.gid=g.gid and r.DID=rd.DID and rd.RELEASEDATE>2013 and r.countid=cn.countid 
and cn.countryname='USA' and r.CLIPID=hg.CLIPID
group by g.genre;

--query d
select fullname from
(select p.fullname, count(ac.clipids) as number_of_clips
from people p, act ac, clips c
where p.pid=ac.PID and ac.CLIPIDS=c.CLIPIDS
group by p.fullname
order by number_of_clips desc
FETCH FIRST 1 ROWS ONLY);

--query e
select number_of_clips from
(select d.pid, count(d.clipids) as number_of_clips
from direct d, clips c
where d.CLIPIDS=c.CLIPIDS
group by d.pid
order by number_of_clips desc
FETCH FIRST 1 ROWS ONLY); 

--query f
select people.fullname
from people
where people.pid in
  (select pid from
    (SELECT distinct pid, Clipids FROM act
    UNION ALL
    SELECT pid, Clipids FROM direct
    UNION ALL
    SELECT pid, Clipids FROM produce
    UNION ALL
    SELECT pid, Clipids FROM write)
  group by pid, clipids
  having count(*) > 1);

--query g
select language from
(select cl.language, count(hl.clipid) as number_of_clips
from clip_languages cl, has_language hl
where cl.lid=hl.lid
group by cl.language
order by number_of_clips desc
FETCH FIRST 10 ROWS ONLY);

-- query h
--suppose the user specified type is Fantasy
select fullname from
(select p.fullname, count(ac.clipids) as number_of_clips
from people p, act ac, clips c, has_genre hg, clip_genre g
where p.pid=ac.PID and ac.CLIPIDS=c.CLIPIDS 
and c.clipids=hg.clipid and hg.gid=g.gid and g.GENRE='Fantasy'
group by p.fullname
order by number_of_clips desc
FETCH FIRST 1 ROWS ONLY);


--MILESTONE 3
CREATE INDEX ind ON act (clipids);
DROP INDEX ind;
--query a
select p.fullname
from people p
where p.pid in
    (select pid from
      (select ac.pid,c.rank, row_number()
      over (partition by ac.pid order by c.rank desc) as index_clip_per_actor
      from clips c, act ac
      where ac.CLIPIDS=c.CLIPIDS and c.votes>100 and ac.pid in
        (select pid from
          (select distinct ac.pid, ac.clipids
          from act ac)
        group by pid
        having count(*)>5))
        where index_clip_per_actor<4
        group by pid
        order by avg(rank) desc
        FETCH FIRST 10 ROWS ONLY);

--query b
select decade, avg(rank) from
  (select clipids, rank, decade,
  row_number() over (partition by decade order by rank desc) as index_clip_per_decade
  from
    (select CLIPIDS, rank, floor(year/ 10) * 10 as decade
    from clips
    where rank is not null and year is not null))
where index_clip_per_decade < 101
group by decade
order by avg(rank) desc;

--query c
select fullname, year, game_titles from
  (select year, fullname, game_titles, 
  row_number() over (partition by fullname order by year) as index_year_per_pid
  from
    (select c.year, p.fullname,
    listagg(c.title, ' , ') within group (order by c.title) as game_titles
    from direct d, clips c, people p
    where d.clipids=c.CLIPIDS and p.pid=d.pid and c.type='VG'
    group by p.fullname,c.year))
where index_year_per_pid=1;

--query d
select year,
listagg(title, ' / ') within group (order by rank desc) as top3_clips_titles,
listagg(rank, ' / ') within group (order by rank desc) as top3_clips_ranks
from
  (select year, title, rank,
  row_number() over (partition by year order by rank desc) as index_clip_per_year
  from clips
  where year is not null and rank is not null)
where index_clip_per_year<4
group by year;

--query e
select p.fullname from people p,
    (with T as
      (select d.pid, c.rank
      from direct d, clips c
      where c.clipids=d.clipids and c.rank is not null
      and d.pid in (select distinct pid from write)
      and d.pid not in
        (select distinct pid from
            (select w.pid, w.clipids from write w
            where (w.pid, w.clipids) not in
            (select ac.pid, ac.clipids from act ac))))
    select T.pid, min(T.rank) as min_rank_directed, max(c.rank) as max_rank_written
    from T, write w, clips c
    where w.pid=T.pid and w.clipids=c.clipids and c.rank is not null
    group by T.pid) S
where S.min_rank_directed > S.max_rank_written+2 and p.pid=S.pid;
  
--query f
select distinct p.fullname
from people p, married m
where p.pid not in m.PID and p.pid in
(select pid from
  (select pid, count(*) as nbr_clips_he_acted_directed from
    (select pid, clipids from
      (select pid, clipids, count(*) as number_of_jobs from
        (SELECT distinct pid, Clipids FROM act
        UNION ALL
        SELECT pid, Clipids FROM direct)
      group by pid , clipids)
    where number_of_jobs>1)
  group by pid)
where nbr_clips_he_acted_directed>2);

--query g
select fullname from people where PID in
  (select w.pid from
    (select w.pid, w.clipids
    from write w 
    where w.worktypes='screenplay') w
  left outer join
    (select p.clipids, count(*) as number_of_producers
    from produce p
    group by p.clipids) p
  on w.clipids=p.clipids
  where number_of_producers>2);


--query h
WITH T As
  (select pid, avg(rank) as average from
    (select distinct ac.pid, ac.clipids, ac.orderscredit, c.rank
    from act ac, clips c
    where orderscredit<4 and orderscredit>0 and rank is not null and c.clipids=ac.clipids)
  group by pid)
SELECT p.fullname, T.average 
FROM people p, T
WHERE p.pid=T.pid;


--query i
select avg(c.rank)
from clips c, has_genre hg,
    (select gid, count(*) as nbr_clips
    from has_genre
    group by gid
    order by nbr_clips desc
    FETCH FIRST 1 ROWS ONLY) s
where hg.clipid=c.CLIPIDS and hg.gid=s.gid
group by hg.gid;

--query j
select p.fullname, l.nbr_of_comedy_clips, l.nbr_of_Drama_clips from people p,
  (select distinct s1.pid, s1.nbr_of_clips_per_genre as nbr_of_comedy_clips, s2.nbr_of_clips_per_genre as nbr_of_Drama_clips
  from
  (select pid, genre, count(*) as nbr_of_clips_per_genre from
    (select pid, cg.genre 
    from act a, clip_genre cg, has_genre hg
    where a.clipids=hg.clipid and hg.gid=cg.gid and (cg.genre='Comedy' or cg.genre='Drama'))
  group by pid, genre) s1
  join
  (select pid, genre, count(*) as nbr_of_clips_per_genre from
    (select pid, cg.genre 
    from act a, clip_genre cg, has_genre hg
    where a.clipids=hg.clipid and hg.gid=cg.gid and (cg.genre='Comedy' or cg.genre='Drama'))
  group by pid, genre) s2
  on s1.pid=s2.pid
  where s1.genre='Comedy' and s1.nbr_of_clips_per_genre>2*s2.nbr_of_clips_per_genre and s2.genre='Drama'
  and s1.pid in
  (select R.pid from
    (select pid, genre, nbr_of_clips_of_actor, count(*) as nbr_of_clips_per_genre
      from
        (with T as
          (select distinct pid, nbr_of_clips_of_actor from
            (select a.pid, count(*) as nbr_of_clips_of_actor
            from act a
            group by a.pid)
          where nbr_of_clips_of_actor>100)
        select T.pid, T.nbr_of_clips_of_actor, cg.genre
        from T, act a, clip_genre cg, has_genre hg
        where T.pid=a.pid and a.clipids=hg.clipid and hg.gid=cg.gid)
    group by pid, genre, nbr_of_clips_of_actor) R
    where (R.nbr_of_clips_per_genre > 0.6 * R.nbr_of_clips_of_actor and R.genre='Short'))) l
where p.pid=l.pid;


--We considered as movies the clips with types 'TV' and 'V'
--query k
select count(*) as nbr_of_movies
from CLIP_LANGUAGES cl, has_language hl, clips c, has_genre hg
where cl.lid=hl.lid and cl.LANGUAGE='Dutch' and c.CLIPIDS=hl.CLIPID and 
c.CLIPIDS=hg.CLIPID and (c.type='V' or c.type='TV') and hg.gid in
  (select gid from
    (select gid, rownum as rn from
      (select gid, count(*) as nbr_clips
      from has_genre
      group by gid
      order by nbr_clips desc
      FETCH FIRST 2 ROWS ONLY))
  where rn=2);

--We considered as movies the clips with types 'TV' and 'V'
--query l
with T as
  (select pid, count(*) as nbr_of_clips from
    (select p.pid, p.CLIPIDS
    from produce p, has_genre hg, clips c
    where p.ROLES='coordinating producer' and p.CLIPIDS=hg.clipid and p.CLIPIDS=c.clipids 
    and (c.type='V' or c.type='TV') and hg.GID in 
      (select gid from
        (select gid, count(*) as nbr_clips
        from has_genre
        group by gid
        order by nbr_clips desc
        FETCH FIRST 1 ROWS ONLY)))
  group by pid
  order by nbr_of_clips desc
  FETCH first 1 row only)
select p.fullname
from people p, T
where p.pid=T.pid;