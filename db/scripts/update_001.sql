create table engine(
	id serial primary key,
	volume double precision not null,
	type varchar(20) not null
);

create table cars(
	id serial primary key,
	model varchar(50) not null,
	created Timestamp not null,
	mile_age integer not null,
	engine_id integer not null references engine(id)
);

create table drivers(
	id serial primary key,
	name varchar(100) not null,
	phone varchar(50) not null
);

create table history_owner(
	id serial primary key,
	driver_id integer not null references drivers(id),
	car_id integer not null references cars(id)
);