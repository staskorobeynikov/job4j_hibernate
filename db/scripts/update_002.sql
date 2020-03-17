create table accounts(
	id serial primary key,
	login varchar(15) not null,
	password varchar(25) not null
);

create table users(
	id serial primary key,
	name varchar(50) not null,
	phone varchar(20) not null,
	address varchar(50) not null,
	account_id integer not null references accounts(id)
);

create table carbodies(
	id serial primary key,
	type varchar(20) not null,
	color varchar(20) not null,
	count_doors integer not null
);

create table engine(
	id serial primary key,
	volume double precision not null,
	power integer not null,
	type varchar(30) not null
);

create table transmission(
	id serial primary key,
	gear_box varchar(30) not null,
	gear_type varchar(30) not null
);

create table marks(
	id serial primary key,
	name varchar(30) not null
);

create table models(
	id serial primary key,
	name varchar(30) not null
);

create table cars(
	id serial primary key,
	mile_age integer not null,
	created Timestamp not null,
	mark_id integer not null references marks(id),
	model_id integer not null references models(id),
	transmission_id integer not null references transmission(id),
	car_body_id integer not null references carbodies(id),
	engine_id integer not null references engine(id)
);

create table adverts(
	id serial primary key,
	price integer not null,
	image_name varchar(100) not null,
	created_date timestamp not null,
	status boolean not null default false,
	owner_id integer not null references users(id),
	car_id integer not null references cars(id)
);