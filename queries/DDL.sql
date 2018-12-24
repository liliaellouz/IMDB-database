CREATE TABLE people(
	pid INTEGER,
	FullName VARCHAR2(128) NOT NULL,
	PRIMARY KEY (pid));
  
CREATE TABLE act(
	pid INTEGER,
	ClipIds INTEGER,
	charid INTEGER,
	OrdersCredit INTEGER,
	AddInfos VARCHAR2(100),
	PRIMARY KEY (pid, ClipIds, charid),
	FOREIGN KEY (pid) REFERENCES People,
	FOREIGN KEY (ClipIds) REFERENCES Clips,
	FOREIGN KEY (charid) REFERENCES Character);
  
CREATE TABLE Direct(
	pid INTEGER,
	ClipIds INTEGER,
	Roles VARCHAR2(90),
	AddInfos VARCHAR2(350),
	PRIMARY KEY (pid, ClipIds),
	FOREIGN KEY (pid) REFERENCES People,
	FOREIGN KEY (ClipIds) REFERENCES Clips );
  
CREATE TABLE Produce(
	pid INTEGER,
	ClipIds INTEGER,
	Roles VARCHAR2(100),
	AddInfos VARCHAR2(250),
	PRIMARY KEY (pid, ClipIds),
	FOREIGN KEY (pid) REFERENCES People,
	FOREIGN KEY (ClipIds) REFERENCES Clips);

CREATE TABLE Write(
	pid INTEGER,
	ClipIds INTEGER,
	Roles VARCHAR2(60),
	AddInfos VARCHAR2(400),
	WorkTypes VARCHAR2(32),
	PRIMARY KEY (pid, ClipIds),
	FOREIGN KEY (pid) REFERENCES People,
	FOREIGN KEY (ClipIds) REFERENCES Clips);

CREATE TABLE Biographical_books(
	bookid INTEGER,
	name VARCHAR2(400) NOT NULL, 
	pid INTEGER,
	PRIMARY KEY (bookid, pid),
	FOREIGN KEY (pid) REFERENCES People (pid) ON DELETE CASCADE);

CREATE TABLE biographies(
  BioId INTEGER,
  pid INTEGER NOT NULL,
  FullName VARCHAR(80), 
  RealName VARCHAR(250),
  Nickname VARCHAR(400),
  DateAndPlaceOfBirth VARCHAR(150),
  Height VARCHAR(20),
  biography CLOB,
  biographer VARCHAR(150),
  DateAndCauseOfDeath VARCHAR(250),
  Trivia CLOB,
  PersonalQuotes CLOB,
  Salary CLOB,
  Trademark CLOB,
  WhereAreTheyNow CLOB,
  PRIMARY KEY (BioId, pid),
  FOREIGN KEY (pid) REFERENCES People (pid) ON DELETE CASCADE);


CREATE TABLE married(
	pid INTEGER,
	sid INTEGER,
  name VARCHAR2(150),
  since INTEGER,
  untill INTEGER,
  children VARCHAR2(80),
  status VARCHAR2(80),
	PRIMARY KEY (pid , sid),
	FOREIGN KEY (pid) REFERENCES People ON DELETE CASCADE);

CREATE TABLE Clips(
	ClipIds INTEGER,
	Title VARCHAR2(400),
	Year INTEGER,
	Type VARCHAR2(20),
	Votes INTEGER,
	Rank FLOAT,
	PRIMARY KEY (ClipIds));

CREATE TABLE Link_types(
	LinkId INTEGER,
	LinkType VARCHAR2(32) NOT NULL,
	PRIMARY KEY(LinkId));

CREATE TABLE Clip_Links(
	ClipFrom INTEGER,
	ClipTo INTEGER,
	LinkId INTEGER,
	PRIMARY KEY (ClipFrom , ClipTo, LinkId),
	FOREIGN KEY (ClipFrom) REFERENCES Clips (ClipIds),
	FOREIGN KEY (ClipTo) REFERENCES Clips (ClipIds),
	FOREIGN KEY (LinkId) REFERENCES Link_types (LinkId));

CREATE TABLE Character(
	charid INTEGER,
	Chars VARCHAR2(800) NOT NULL, 
	PRIMARY KEY (charid));

CREATE TABLE Clip_languages(
	lid INTEGER,
	Langage VARCHAR2(80) NOT NULL,
	PRIMARY KEY (lid));

CREATE TABLE has_language(
	ClipId INTEGER,
	lid INTEGER,
	PRIMARY KEY (ClipId,lid),
	FOREIGN KEY (ClipId) REFERENCES Clips(ClipIds),
	FOREIGN KEY (lid) REFERENCES Clip_languages(lid));


CREATE TABLE countries(
	countid INTEGER,	
	CountryName VARCHAR2(50) NOT NULL,
	PRIMARY KEY(countid));

CREATE TABLE filmed_in(
	ClipId INTEGER,
	countid INTEGER,
	PRIMARY KEY (ClipId, countid),
	FOREIGN KEY (ClipId) REFERENCES Clips(ClipIds),
	FOREIGN KEY (countid) REFERENCES Countries(countid));

CREATE TABLE released_in(
	ClipId INTEGER,
	countid INTEGER,
	did INTEGER,
	PRIMARY KEY (ClipId, countid, did),
	FOREIGN KEY (ClipId) REFERENCES Clips(ClipIds),
	FOREIGN KEY (countid) REFERENCES Countries(countid),
	FOREIGN KEY (did) REFERENCES Releasedates(did));

CREATE TABLE Releasedates(
	did INTEGER,	
	Releasedate INTEGER NOT NULL,
	PRIMARY KEY(did));

CREATE TABLE Clip_genre(
	gid INTEGER,
	Genre VARCHAR2(20) NOT NULL,
	PRIMARY KEY (gid));

CREATE TABLE Has_genre(
	ClipId INTEGER,
	gid INTEGER,
	PRIMARY KEY (ClipId,gid),
	FOREIGN KEY (ClipId) REFERENCES Clips(ClipIds),
	FOREIGN KEY (gid) REFERENCES Clip_genre(gid));
  
  CREATE TABLE Runningtimes(
	rid INTEGER,	
	runtime INTEGER NOT NULL,
	PRIMARY KEY(rid));

CREATE TABLE Runs_for(
	ClipId INTEGER,
	countid INTEGER,
	rid INTEGER,
	PRIMARY KEY (ClipId, countid, rid),
	FOREIGN KEY (ClipId) REFERENCES Clips(ClipIds),
	FOREIGN KEY (countid) REFERENCES Countries(countid),
	FOREIGN KEY (rid) REFERENCES Runningtimes(rid));
