# --- !Ups

-- Creating NFRs
INSERT INTO NFR ( ID , DESCRIPTION ) VALUES (nextval('entity_seq'), 'Das Kassen-System für die Abrechnung muss 99.99% im Jahr verfügbar sein');


# --- !Downs

delete from NFR;
