SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;


CREATE TYPE kycing.gender AS ENUM (
    'male',
    'female',
    'other'
);


ALTER TYPE kycing.gender OWNER TO jeus;

CREATE TYPE kycing.kycstatus AS ENUM (
    'pending',
    'accepted',
    'checking',
    'rejected'
);


ALTER TYPE kycing.kycstatus OWNER TO jeus;

CREATE TYPE kycing.licensetype AS ENUM (
    'DL',
    'PS',
    'NI'
);


ALTER TYPE kycing.licensetype OWNER TO jeus;

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TABLE kycing.country (
    id character varying(2) NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE kycing.country OWNER TO jeus;


COMMENT ON TABLE kycing.country IS 'country that user can select. ';

CREATE SEQUENCE kycing.kycinfo_id_seq
    START WITH 9999
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE kycing.kycinfo
(
    id bigint NOT NULL DEFAULT nextval('kycing.kycinfo_id_seq'::regclass),
    uid character varying(36) COLLATE pg_catalog."default" NOT NULL,
    ltype kycing.licensetype NOT NULL,
    fname character varying(40) COLLATE pg_catalog."default" NOT NULL,
    lname character varying(40) COLLATE pg_catalog."default" NOT NULL,
    licenseid character varying(40) COLLATE pg_catalog."default" NOT NULL,
    gender kycing.gender,
    lastupdate timestamp without time zone DEFAULT now(),
    status kycing.kycstatus NOT NULL DEFAULT 'pending'::kycing.kycstatus,
    country character varying(2) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT kycinfo_pkey PRIMARY KEY (id)
);

ALTER TABLE kycing.kycinfo OWNER TO jeus;
COMMENT ON TABLE kycing.kycinfo IS 'information user kyc.';
COMMENT ON COLUMN kycing.kycinfo.ltype IS 'license type is passport/drivers license/ National ID Card';
COMMENT ON COLUMN kycing.kycinfo.fname  IS 'first name';
COMMENT ON COLUMN kycing.kycinfo.lname IS 'last name';
COMMENT ON COLUMN kycing.kycinfo.licenseid IS 'passport / driving code and ...  id';
COMMENT ON COLUMN kycing.kycinfo.status IS '''not_active'',''pending'',''checking'',''accepted'',''rejected''';

INSERT INTO kycing.country (id, name) VALUES ('AF', 'Afghanistan');
INSERT INTO kycing.country (id, name) VALUES ('AX', 'Åland Islands');
INSERT INTO kycing.country (id, name) VALUES ('AL', 'Albania');
INSERT INTO kycing.country (id, name) VALUES ('DZ', 'Algeria');
INSERT INTO kycing.country (id, name) VALUES ('AS', 'American Samoa');
INSERT INTO kycing.country (id, name) VALUES ('AD', 'AndorrA');
INSERT INTO kycing.country (id, name) VALUES ('AO', 'Angola');
INSERT INTO kycing.country (id, name) VALUES ('AI', 'Anguilla');
INSERT INTO kycing.country (id, name) VALUES ('AQ', 'Antarctica');
INSERT INTO kycing.country (id, name) VALUES ('AG', 'Antigua and Barbuda');
INSERT INTO kycing.country (id, name) VALUES ('AR', 'Argentina');
INSERT INTO kycing.country (id, name) VALUES ('AM', 'Armenia');
INSERT INTO kycing.country (id, name) VALUES ('AW', 'Aruba');
INSERT INTO kycing.country (id, name) VALUES ('AU', 'Australia');
INSERT INTO kycing.country (id, name) VALUES ('AT', 'Austria');
INSERT INTO kycing.country (id, name) VALUES ('AZ', 'Azerbaijan');
INSERT INTO kycing.country (id, name) VALUES ('BS', 'Bahamas');
INSERT INTO kycing.country (id, name) VALUES ('BH', 'Bahrain');
INSERT INTO kycing.country (id, name) VALUES ('BD', 'Bangladesh');
INSERT INTO kycing.country (id, name) VALUES ('BB', 'Barbados');
INSERT INTO kycing.country (id, name) VALUES ('BY', 'Belarus');
INSERT INTO kycing.country (id, name) VALUES ('BE', 'Belgium');
INSERT INTO kycing.country (id, name) VALUES ('BZ', 'Belize');
INSERT INTO kycing.country (id, name) VALUES ('BJ', 'Benin');
INSERT INTO kycing.country (id, name) VALUES ('BM', 'Bermuda');
INSERT INTO kycing.country (id, name) VALUES ('BT', 'Bhutan');
INSERT INTO kycing.country (id, name) VALUES ('BO', 'Bolivia');
INSERT INTO kycing.country (id, name) VALUES ('BA', 'Bosnia and Herzegovina');
INSERT INTO kycing.country (id, name) VALUES ('BW', 'Botswana');
INSERT INTO kycing.country (id, name) VALUES ('BV', 'Bouvet Island');
INSERT INTO kycing.country (id, name) VALUES ('BR', 'Brazil');
INSERT INTO kycing.country (id, name) VALUES ('IO', 'British Indian Ocean Territory');
INSERT INTO kycing.country (id, name) VALUES ('BN', 'Brunei Darussalam');
INSERT INTO kycing.country (id, name) VALUES ('BG', 'Bulgaria');
INSERT INTO kycing.country (id, name) VALUES ('BF', 'Burkina Faso');
INSERT INTO kycing.country (id, name) VALUES ('BI', 'Burundi');
INSERT INTO kycing.country (id, name) VALUES ('KH', 'Cambodia');
INSERT INTO kycing.country (id, name) VALUES ('CM', 'Cameroon');
INSERT INTO kycing.country (id, name) VALUES ('CA', 'Canada');
INSERT INTO kycing.country (id, name) VALUES ('CV', 'Cape Verde');
INSERT INTO kycing.country (id, name) VALUES ('KY', 'Cayman Islands');
INSERT INTO kycing.country (id, name) VALUES ('CF', 'Central African Republic');
INSERT INTO kycing.country (id, name) VALUES ('TD', 'Chad');
INSERT INTO kycing.country (id, name) VALUES ('CL', 'Chile');
INSERT INTO kycing.country (id, name) VALUES ('CN', 'China');
INSERT INTO kycing.country (id, name) VALUES ('CX', 'Christmas Island');
INSERT INTO kycing.country (id, name) VALUES ('CC', 'Cocos (Keeling) Islands');
INSERT INTO kycing.country (id, name) VALUES ('CO', 'Colombia');
INSERT INTO kycing.country (id, name) VALUES ('KM', 'Comoros');
INSERT INTO kycing.country (id, name) VALUES ('CG', 'Congo');
INSERT INTO kycing.country (id, name) VALUES ('CD', 'Congo, Democratic Republic');
INSERT INTO kycing.country (id, name) VALUES ('CK', 'Cook Islands');
INSERT INTO kycing.country (id, name) VALUES ('CR', 'Costa Rica');
INSERT INTO kycing.country (id, name) VALUES ('CI', 'Cote D"Ivoire');
INSERT INTO kycing.country (id, name) VALUES ('HR', 'Croatia');
INSERT INTO kycing.country (id, name) VALUES ('CU', 'Cuba');
INSERT INTO kycing.country (id, name) VALUES ('CY', 'Cyprus');
INSERT INTO kycing.country (id, name) VALUES ('CZ', 'Czech Republic');
INSERT INTO kycing.country (id, name) VALUES ('DK', 'Denmark');
INSERT INTO kycing.country (id, name) VALUES ('DJ', 'Djibouti');
INSERT INTO kycing.country (id, name) VALUES ('DM', 'Dominica');
INSERT INTO kycing.country (id, name) VALUES ('DO', 'Dominican Republic');
INSERT INTO kycing.country (id, name) VALUES ('EC', 'Ecuador');
INSERT INTO kycing.country (id, name) VALUES ('EG', 'Egypt');
INSERT INTO kycing.country (id, name) VALUES ('SV', 'El Salvador');
INSERT INTO kycing.country (id, name) VALUES ('GQ', 'Equatorial Guinea');
INSERT INTO kycing.country (id, name) VALUES ('ER', 'Eritrea');
INSERT INTO kycing.country (id, name) VALUES ('EE', 'Estonia');
INSERT INTO kycing.country (id, name) VALUES ('ET', 'Ethiopia');
INSERT INTO kycing.country (id, name) VALUES ('FK', 'Falkland Islands (Malvinas)');
INSERT INTO kycing.country (id, name) VALUES ('FO', 'Faroe Islands');
INSERT INTO kycing.country (id, name) VALUES ('FJ', 'Fiji');
INSERT INTO kycing.country (id, name) VALUES ('FI', 'Finland');
INSERT INTO kycing.country (id, name) VALUES ('FR', 'France');
INSERT INTO kycing.country (id, name) VALUES ('GF', 'French Guiana');
INSERT INTO kycing.country (id, name) VALUES ('PF', 'French Polynesia');
INSERT INTO kycing.country (id, name) VALUES ('TF', 'French Southern Territories');
INSERT INTO kycing.country (id, name) VALUES ('GA', 'Gabon');
INSERT INTO kycing.country (id, name) VALUES ('GM', 'Gambia');
INSERT INTO kycing.country (id, name) VALUES ('GE', 'Georgia');
INSERT INTO kycing.country (id, name) VALUES ('DE', 'Germany');
INSERT INTO kycing.country (id, name) VALUES ('GH', 'Ghana');
INSERT INTO kycing.country (id, name) VALUES ('GI', 'Gibraltar');
INSERT INTO kycing.country (id, name) VALUES ('GR', 'Greece');
INSERT INTO kycing.country (id, name) VALUES ('GL', 'Greenland');
INSERT INTO kycing.country (id, name) VALUES ('GD', 'Grenada');
INSERT INTO kycing.country (id, name) VALUES ('GP', 'Guadeloupe');
INSERT INTO kycing.country (id, name) VALUES ('GU', 'Guam');
INSERT INTO kycing.country (id, name) VALUES ('GT', 'Guatemala');
INSERT INTO kycing.country (id, name) VALUES ('GG', 'Guernsey');
INSERT INTO kycing.country (id, name) VALUES ('GN', 'Guinea');
INSERT INTO kycing.country (id, name) VALUES ('GW', 'Guinea-Bissau');
INSERT INTO kycing.country (id, name) VALUES ('GY', 'Guyana');
INSERT INTO kycing.country (id, name) VALUES ('HT', 'Haiti');
INSERT INTO kycing.country (id, name) VALUES ('HM', 'Heard Island and Mcdonald Islands');
INSERT INTO kycing.country (id, name) VALUES ('VA', 'Holy See (Vatican City State)');
INSERT INTO kycing.country (id, name) VALUES ('HN', 'Honduras');
INSERT INTO kycing.country (id, name) VALUES ('HK', 'Hong Kong');
INSERT INTO kycing.country (id, name) VALUES ('HU', 'Hungary');
INSERT INTO kycing.country (id, name) VALUES ('IS', 'Iceland');
INSERT INTO kycing.country (id, name) VALUES ('IN', 'India');
INSERT INTO kycing.country (id, name) VALUES ('ID', 'Indonesia');
INSERT INTO kycing.country (id, name) VALUES ('IR', 'Iran');
INSERT INTO kycing.country (id, name) VALUES ('IQ', 'Iraq');
INSERT INTO kycing.country (id, name) VALUES ('IE', 'Ireland');
INSERT INTO kycing.country (id, name) VALUES ('IM', 'Isle of Man');
INSERT INTO kycing.country (id, name) VALUES ('IL', 'Israel');
INSERT INTO kycing.country (id, name) VALUES ('IT', 'Italy');
INSERT INTO kycing.country (id, name) VALUES ('JM', 'Jamaica');
INSERT INTO kycing.country (id, name) VALUES ('JP', 'Japan');
INSERT INTO kycing.country (id, name) VALUES ('JE', 'Jersey');
INSERT INTO kycing.country (id, name) VALUES ('JO', 'Jordan');
INSERT INTO kycing.country (id, name) VALUES ('KZ', 'Kazakhstan');
INSERT INTO kycing.country (id, name) VALUES ('KE', 'Kenya');
INSERT INTO kycing.country (id, name) VALUES ('KI', 'Kiribati');
INSERT INTO kycing.country (id, name) VALUES ('KP', 'Korea (North)');
INSERT INTO kycing.country (id, name) VALUES ('KR', 'Korea (South)');
INSERT INTO kycing.country (id, name) VALUES ('XK', 'Kosovo');
INSERT INTO kycing.country (id, name) VALUES ('KW', 'Kuwait');
INSERT INTO kycing.country (id, name) VALUES ('KG', 'Kyrgyzstan');
INSERT INTO kycing.country (id, name) VALUES ('LA', 'Laos');
INSERT INTO kycing.country (id, name) VALUES ('LV', 'Latvia');
INSERT INTO kycing.country (id, name) VALUES ('LB', 'Lebanon');
INSERT INTO kycing.country (id, name) VALUES ('LS', 'Lesotho');
INSERT INTO kycing.country (id, name) VALUES ('LR', 'Liberia');
INSERT INTO kycing.country (id, name) VALUES ('LY', 'Libyan Arab Jamahiriya');
INSERT INTO kycing.country (id, name) VALUES ('LI', 'Liechtenstein');
INSERT INTO kycing.country (id, name) VALUES ('LT', 'Lithuania');
INSERT INTO kycing.country (id, name) VALUES ('LU', 'Luxembourg');
INSERT INTO kycing.country (id, name) VALUES ('MO', 'Macao');
INSERT INTO kycing.country (id, name) VALUES ('MK', 'Macedonia');
INSERT INTO kycing.country (id, name) VALUES ('MG', 'Madagascar');
INSERT INTO kycing.country (id, name) VALUES ('MW', 'Malawi');
INSERT INTO kycing.country (id, name) VALUES ('MY', 'Malaysia');
INSERT INTO kycing.country (id, name) VALUES ('MV', 'Maldives');
INSERT INTO kycing.country (id, name) VALUES ('ML', 'Mali');
INSERT INTO kycing.country (id, name) VALUES ('MT', 'Malta');
INSERT INTO kycing.country (id, name) VALUES ('MH', 'Marshall Islands');
INSERT INTO kycing.country (id, name) VALUES ('MQ', 'Martinique');
INSERT INTO kycing.country (id, name) VALUES ('MR', 'Mauritania');
INSERT INTO kycing.country (id, name) VALUES ('MU', 'Mauritius');
INSERT INTO kycing.country (id, name) VALUES ('YT', 'Mayotte');
INSERT INTO kycing.country (id, name) VALUES ('MX', 'Mexico');
INSERT INTO kycing.country (id, name) VALUES ('FM', 'Micronesia');
INSERT INTO kycing.country (id, name) VALUES ('MD', 'Moldova');
INSERT INTO kycing.country (id, name) VALUES ('MC', 'Monaco');
INSERT INTO kycing.country (id, name) VALUES ('MN', 'Mongolia');
INSERT INTO kycing.country (id, name) VALUES ('MS', 'Montserrat');
INSERT INTO kycing.country (id, name) VALUES ('MA', 'Morocco');
INSERT INTO kycing.country (id, name) VALUES ('MZ', 'Mozambique');
INSERT INTO kycing.country (id, name) VALUES ('MM', 'Myanmar');
INSERT INTO kycing.country (id, name) VALUES ('NA', 'Namibia');
INSERT INTO kycing.country (id, name) VALUES ('NR', 'Nauru');
INSERT INTO kycing.country (id, name) VALUES ('NP', 'Nepal');
INSERT INTO kycing.country (id, name) VALUES ('NL', 'Netherlands');
INSERT INTO kycing.country (id, name) VALUES ('AN', 'Netherlands Antilles');
INSERT INTO kycing.country (id, name) VALUES ('NC', 'New Caledonia');
INSERT INTO kycing.country (id, name) VALUES ('NZ', 'New Zealand');
INSERT INTO kycing.country (id, name) VALUES ('NI', 'Nicaragua');
INSERT INTO kycing.country (id, name) VALUES ('NE', 'Niger');
INSERT INTO kycing.country (id, name) VALUES ('NG', 'Nigeria');
INSERT INTO kycing.country (id, name) VALUES ('NU', 'Niue');
INSERT INTO kycing.country (id, name) VALUES ('NF', 'Norfolk Island');
INSERT INTO kycing.country (id, name) VALUES ('MP', 'Northern Mariana Islands');
INSERT INTO kycing.country (id, name) VALUES ('NO', 'Norway');
INSERT INTO kycing.country (id, name) VALUES ('OM', 'Oman');
INSERT INTO kycing.country (id, name) VALUES ('PK', 'Pakistan');
INSERT INTO kycing.country (id, name) VALUES ('PW', 'Palau');
INSERT INTO kycing.country (id, name) VALUES ('PS', 'Palestinian Territory, Occupied');
INSERT INTO kycing.country (id, name) VALUES ('PA', 'Panama');
INSERT INTO kycing.country (id, name) VALUES ('PG', 'Papua New Guinea');
INSERT INTO kycing.country (id, name) VALUES ('PY', 'Paraguay');
INSERT INTO kycing.country (id, name) VALUES ('PE', 'Peru');
INSERT INTO kycing.country (id, name) VALUES ('PH', 'Philippines');
INSERT INTO kycing.country (id, name) VALUES ('PN', 'Pitcairn');
INSERT INTO kycing.country (id, name) VALUES ('PL', 'Poland');
INSERT INTO kycing.country (id, name) VALUES ('PT', 'Portugal');
INSERT INTO kycing.country (id, name) VALUES ('PR', 'Puerto Rico');
INSERT INTO kycing.country (id, name) VALUES ('QA', 'Qatar');
INSERT INTO kycing.country (id, name) VALUES ('RE', 'Reunion');
INSERT INTO kycing.country (id, name) VALUES ('RO', 'Romania');
INSERT INTO kycing.country (id, name) VALUES ('RU', 'Russian Federation');
INSERT INTO kycing.country (id, name) VALUES ('RW', 'Rwanda');
INSERT INTO kycing.country (id, name) VALUES ('SH', 'Saint Helena');
INSERT INTO kycing.country (id, name) VALUES ('KN', 'Saint Kitts and Nevis');
INSERT INTO kycing.country (id, name) VALUES ('LC', 'Saint Lucia');
INSERT INTO kycing.country (id, name) VALUES ('PM', 'Saint Pierre and Miquelon');
INSERT INTO kycing.country (id, name) VALUES ('VC', 'Saint Vincent and the Grenadines');
INSERT INTO kycing.country (id, name) VALUES ('WS', 'Samoa');
INSERT INTO kycing.country (id, name) VALUES ('SM', 'San Marino');
INSERT INTO kycing.country (id, name) VALUES ('ST', 'Sao Tome and Principe');
INSERT INTO kycing.country (id, name) VALUES ('SA', 'Saudi Arabia');
INSERT INTO kycing.country (id, name) VALUES ('SN', 'Senegal');
INSERT INTO kycing.country (id, name) VALUES ('RS', 'Serbia');
INSERT INTO kycing.country (id, name) VALUES ('ME', 'Montenegro');
INSERT INTO kycing.country (id, name) VALUES ('SC', 'Seychelles');
INSERT INTO kycing.country (id, name) VALUES ('SL', 'Sierra Leone');
INSERT INTO kycing.country (id, name) VALUES ('SG', 'Singapore');
INSERT INTO kycing.country (id, name) VALUES ('SK', 'Slovakia');
INSERT INTO kycing.country (id, name) VALUES ('SI', 'Slovenia');
INSERT INTO kycing.country (id, name) VALUES ('SB', 'Solomon Islands');
INSERT INTO kycing.country (id, name) VALUES ('SO', 'Somalia');
INSERT INTO kycing.country (id, name) VALUES ('ZA', 'South Africa');
INSERT INTO kycing.country (id, name) VALUES ('GS', 'South Georgia and the South Sandwich Islands');
INSERT INTO kycing.country (id, name) VALUES ('ES', 'Spain');
INSERT INTO kycing.country (id, name) VALUES ('LK', 'Sri Lanka');
INSERT INTO kycing.country (id, name) VALUES ('SD', 'Sudan');
INSERT INTO kycing.country (id, name) VALUES ('SR', 'Suriname');
INSERT INTO kycing.country (id, name) VALUES ('SJ', 'Svalbard and Jan Mayen');
INSERT INTO kycing.country (id, name) VALUES ('SZ', 'Swaziland');
INSERT INTO kycing.country (id, name) VALUES ('SE', 'Sweden');
INSERT INTO kycing.country (id, name) VALUES ('CH', 'Switzerland');
INSERT INTO kycing.country (id, name) VALUES ('SY', 'Syrian Arab Republic');
INSERT INTO kycing.country (id, name) VALUES ('TW', 'Taiwan, Province of China');
INSERT INTO kycing.country (id, name) VALUES ('TJ', 'Tajikistan');
INSERT INTO kycing.country (id, name) VALUES ('TZ', 'Tanzania');
INSERT INTO kycing.country (id, name) VALUES ('TH', 'Thailand');
INSERT INTO kycing.country (id, name) VALUES ('TL', 'Timor-Leste');
INSERT INTO kycing.country (id, name) VALUES ('TG', 'Togo');
INSERT INTO kycing.country (id, name) VALUES ('TK', 'Tokelau');
INSERT INTO kycing.country (id, name) VALUES ('TO', 'Tonga');
INSERT INTO kycing.country (id, name) VALUES ('TT', 'Trinidad and Tobago');
INSERT INTO kycing.country (id, name) VALUES ('TN', 'Tunisia');
INSERT INTO kycing.country (id, name) VALUES ('TR', 'Turkey');
INSERT INTO kycing.country (id, name) VALUES ('TM', 'Turkmenistan');
INSERT INTO kycing.country (id, name) VALUES ('TC', 'Turks and Caicos Islands');
INSERT INTO kycing.country (id, name) VALUES ('TV', 'Tuvalu');
INSERT INTO kycing.country (id, name) VALUES ('UG', 'Uganda');
INSERT INTO kycing.country (id, name) VALUES ('UA', 'Ukraine');
INSERT INTO kycing.country (id, name) VALUES ('AE', 'United Arab Emirates');
INSERT INTO kycing.country (id, name) VALUES ('GB', 'United Kingdom');
INSERT INTO kycing.country (id, name) VALUES ('US', 'United States');
INSERT INTO kycing.country (id, name) VALUES ('UM', 'United States Minor Outlying Islands');
INSERT INTO kycing.country (id, name) VALUES ('UY', 'Uruguay');
INSERT INTO kycing.country (id, name) VALUES ('UZ', 'Uzbekistan');
INSERT INTO kycing.country (id, name) VALUES ('VU', 'Vanuatu');
INSERT INTO kycing.country (id, name) VALUES ('VE', 'Venezuela');
INSERT INTO kycing.country (id, name) VALUES ('VN', 'Viet Nam');
INSERT INTO kycing.country (id, name) VALUES ('VG', 'Virgin Islands, British');
INSERT INTO kycing.country (id, name) VALUES ('VI', 'Virgin Islands, U.S.');
INSERT INTO kycing.country (id, name) VALUES ('WF', 'Wallis and Futuna');
INSERT INTO kycing.country (id, name) VALUES ('EH', 'Western Sahara');
INSERT INTO kycing.country (id, name) VALUES ('YE', 'Yemen');
INSERT INTO kycing.country (id, name) VALUES ('ZM', 'Zambia');
INSERT INTO kycing.country (id, name) VALUES ('ZW', 'Zimbabwe');


ALTER TABLE ONLY kycing.country ADD CONSTRAINT country_pkey PRIMARY KEY (id);




