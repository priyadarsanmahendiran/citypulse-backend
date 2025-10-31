CREATE TABLE public.city (
	id int GENERATED ALWAYS AS IDENTITY NOT NULL,
	city_name varchar NOT NULL,
	country varchar NOT NULL,
	CONSTRAINT city_pk PRIMARY KEY (id)
);
