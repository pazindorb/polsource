delete from versions;
delete from notes;

insert into notes(note_id, created, active) values(999, parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'), true);
insert into notes(note_id, created, active) values(998, parsedatetime('12-10-2020 10:20:52.84', 'dd-MM-yyyy hh:mm:ss.SS'), true);

insert into versions(version_id, title, content, modified, version_number, note_id)
values(9997, 'First Note', 'Content of first note', parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'), 1, 999);

insert into versions(version_id, title, content, modified, version_number, note_id)
values(9998, 'Second Note', 'Content of second note v1', parsedatetime('12-10-2020 10:20:52.84', 'dd-MM-yyyy hh:mm:ss.SS'), 1, 998);

insert into versions(version_id, title, content, modified, version_number, note_id)
values(9999, 'Second Note', 'Content of second note v2', parsedatetime('10-11-2021 13:20:52.84', 'dd-MM-yyyy hh:mm:ss.SS'), 2, 998);