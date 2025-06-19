insert into customer(customer_id, customer_name) values (1, 'Maureen Green');
insert into customer(customer_id, customer_name) values (2, 'Justin Black');
insert into customer(customer_id, customer_name) values (3, 'Jenny Freeman');
insert into customer(customer_id, customer_name) values (4, 'Thomas Roberts');

select setval('customer_customer_id_seq', (select max(customer_id) from customer));


insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(1, 'London,UK', '2022-05-20', '2022-05-31', 5478);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(1, 'Barcelona,Spain', '2022-07-12', '2022-07-22', 3200);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(1, 'Dublin,Ireland', '2021-08-01', '2021-08-12', 4355);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(1, 'Paris,France', '2020-04-15', '2020-04-30', 2243);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(1, 'London,UK', '2020-02-02', '2020-02-15', 1999);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(3, 'Hong Kong', '2023-07-04', '2023-07-15', 1876);

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(3, 'Tokyo,Japan', '2023-07-16', '2023-07-20', 899);
