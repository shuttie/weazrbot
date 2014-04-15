create table weather (
  zone int,
  supplier varchar,
  run_at timestamp,
  forecast_for timestamp,
  lat float,
  lon float,
  tmp float,
  rh float,
  precip float,
  uwind float,
  vwind float,
  primary key ((zone),supplier,run_at,forecast_for,lat,lon)
);

create table runs (
  supplier varchar,
  run_at timestamp,
  analyzed_at timestamp,
  primary key ((supplier), run_at)
) with clustering order by (run_at desc);

create table cities (
  id int,
  name text,
  alt_names set<text>,
  lat float,
  lon float,
  zones set<int>,
  primary key (id)
);

create index on weazr.cities (name);