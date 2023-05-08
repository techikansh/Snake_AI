create table scoreboard(
name varchar(60),
score int
);

select * 
from scoreboard
order by score desc;

INSERT INTO scoreboard(NAME,SCORE)
VALUES("Devansh",12);


delete 
from scorebaord
;

DELETE 
FROM scorebaord 
WHERE name in ('ekta', 'dev');

drop table scorebaord