CREATE TABLE IF NOT EXISTS hero
(
    hero_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    house VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS way_point
(
    way_point_id    BIGSERIAL PRIMARY KEY,
    hero_id BIGSERIAL REFERENCES hero (hero_id),
    x DOUBLE PRECISION NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    velocity DOUBLE PRECISION NOT NULL,
    delay_millis INTEGER NOT NULL
);

create sequence if not exists hero_seq start 1;
alter table hero alter COLUMN hero_id SET DEFAULT nextval('hero_seq');

create sequence if not exists way_point_seq start 1;
alter table way_point alter COLUMN way_point_id SET DEFAULT nextval('way_point_seq');