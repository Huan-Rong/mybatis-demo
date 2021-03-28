create table user
(
	id int auto_increment
		primary key,
	last_name varchar(255) not null,
	gender char null,
	email varchar(255) null
);