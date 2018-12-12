#!/usr/bin/env bash
psql -h localhost -U jeus -d jeus -c "DROP SCHEMA invoicing CASCADE;"
psql -U jeus -d jeus -n kycing -1 -f kycing_batch.sql