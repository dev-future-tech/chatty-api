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

insert into customer_booking(customer_id, destination, start_date, end_date, total_cost)
values(3, 'Bangkok,Thailand', '2019-05-03', '2019-05-20', 798);

insert into destinations(city, description)
values('London,UK', 'London is a vibrant and diverse global city known for its rich history, iconic landmarks, and world-class cultural institutions.');

insert into destinations(city, description)
values ('Barcelona,Spain', 'Barcelona is a bustling coastal city renowned for its stunning beaches, unique modernist architecture, and vibrant cultural heritage.');

insert into destinations(city, description)
values ('Dublin,Ireland', 'Dublin is a charming and historic capital city celebrated for its friendly people, rich literary heritage, and iconic landmarks such as Trinity College and Temple Bar.');

insert into destinations(city, description)
values ('Paris,France', 'Paris is the romantic City of Light, famous for its stunning architecture, world-class art museums, and picturesque Seine River waterfront.');

insert into destinations(city, description)
values ('Hong Kong', 'Hong Kong is a dynamic and cosmopolitan metropolis that seamlessly blends traditional Chinese culture with modern finance, commerce, and breathtaking cityscape views.');

insert into destinations(city, description)
values ('Tokyo,Japan', 'Tokyo is a vibrant and futuristic capital city that seamlessly blends ancient traditions with cutting-edge technology, fashion, and innovative culinary delights.');

insert into destinations (city, description)
values ('Bangkok,Thailand', 'Bangkok is a bustling and exotic metropolis known for its ornate temples, floating markets, spicy street food, and vibrant nightlife along the Chao Phraya River.');



-- Update Airports

insert into airports(airport_name, timezone) values('Narita Airport (NRT)', '+9');
insert into airports(airport_name, timezone) values('Charles de Gaulle (CDG)', '0');
insert into airports(airport_name, timezone) values('Dublin Airport', '0');
insert into airports(airport_name, timezone) values('Heathrow Airport (LHR)', '0');
insert into airports(airport_name, timezone) values('Josep Tarradellas Barcelona-El Prat Airport (BCN)', '+2');
insert into airports(airport_name, timezone) values('Hong Kong International Airport (HKG)', '+8');
insert into airports(airport_name, timezone) values('Suvarnabhumi Airport (BKK)', '+7');


insert into flights(origin_id, destination_id, airport_id, departure_date, departure_time, arrival_date, arrival_time)
values(6, 1, 1, '2025-08-01', '0900', '2025-08-01', '1600');
