ALTER TABLE kycing.kycinfo ADD COLUMN address character varying(200) NOT NULL DEFAULT 'UNDEFINED';
COMMENT ON COLUMN kycing.kycinfo.address IS 'merchant address or user address.';

ALTER TABLE kycing.kycinfo ADD COLUMN card character varying(16) NOT NULL DEFAULT 'UNDEFINED';
COMMENT ON COLUMN kycing.kycinfo.card IS 'merchant card number.';