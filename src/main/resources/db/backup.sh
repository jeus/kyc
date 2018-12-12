#!/usr/bin/env bash

#get backup by copy command (fast and smal)
pg_dump -h localhost -p 5432 -U jeus -n kycing -b -v -f kycing_batch.sql

#get backup by insert command (slower and larger)
pg_dump -h localhost -p 5432 -U jeus -n kycing --column-inserts -b -v -f kycing_insert.sql
