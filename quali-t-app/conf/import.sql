--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO "user" VALUES (1000, '78c036a9bfc08d0c4150a9b9af9dd61c79ab4b8a', 'admin', 'eATliOMyVQhhmOLINQHO');
INSERT INTO "user" VALUES (1001, '994b8e7341be4aff15db7412cdafb4e33ac64eb7', 'corina', 'wUScbpcsYHyLjEQTHTmT');
INSERT INTO "user" VALUES (1002, '4dd3a08cdda45838c11591789b4a47fedc1d3cc9', 'emre.avsar92@gmail.com', 'YqYpSzIoxBmpOliRctnV');
INSERT INTO "user" VALUES (1003, 'd5506a1c7feda5c81f98f73cb547821d82d04d60', 'ozimmermann', 'zAmVyCdklHphTtkyJuKM');

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO role VALUES (2000, 'admin');
INSERT INTO role VALUES (2001, 'dashboard');

--
-- Data for Name: role_user; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO role_user VALUES (1000, 2000);
INSERT INTO role_user VALUES (1000, 2001);
INSERT INTO role_user VALUES (1001, 2000);
INSERT INTO role_user VALUES (1001, 2001);
INSERT INTO role_user VALUES (1002, 2000);
INSERT INTO role_user VALUES (1002, 2001);
INSERT INTO role_user VALUES (1003, 2000);
INSERT INTO role_user VALUES (1003, 2001);

--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO customer (id, address, name) VALUES (3000, 'Zürich', 'UBS');
INSERT INTO customer (id, address, name) VALUES (3001, 'Rapperswil', 'HSR');

--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO project (id, name, projectcustomer_id) VALUES (11000, 'Campus Mobile App (iOS)', 3001);
INSERT INTO project (id, name, projectcustomer_id) VALUES (11001, 'Cloud Banking', 3000);

--
-- Data for Name: favorite_project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO favorite_project (user_id, project_id) VALUES (1000, 11000);
INSERT INTO favorite_project (user_id, project_id) VALUES (1000, 11001);
INSERT INTO favorite_project (user_id, project_id) VALUES (1001, 11001);

--
-- Data for Name: qacategory; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qacategory VALUES (4000, 'fa fa-cog', 'Functionality', NULL);
INSERT INTO qacategory VALUES (4001, 'fa fa-heart', 'Usability', NULL);
INSERT INTO qacategory VALUES (4002, 'fa fa-heart', 'Portability', 4001);

--
-- Data for Name: qa; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qa VALUES (5000, '<p>Das System soll ______ % verfügbar sein.</p>');
INSERT INTO qa VALUES (5001, '<p>The _________ System is ______ of the year <b>available.</b></p>');+

--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO task (id, description, done, assignee_id) VALUES (6000, 'Evaluate Catalog HSR Mobile App', true, 1000);
INSERT INTO task (id, description, done, assignee_id) VALUES (6001, 'Create Catalog for Cloud Apps', false, 1000);

--
-- Data for Name: catalog; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO catalog VALUES (6000, 'Dies ist der Standardkatalog', 'Standard Katalog', NULL);
INSERT INTO catalog VALUES (6001, 'Katalog für Cloud Templates', 'Cloud Katalog', NULL);

--
-- Data for Name: catalogqa; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO catalogqa VALUES (7000, 6000, 5000);
INSERT INTO catalogqa VALUES (7001, 6000, 5001);
INSERT INTO catalogqa VALUES (7002, 6001, 5000);

--
-- Data for Name: qualityproperty; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qualityproperty VALUES (8000, 'Specific', 'S');
INSERT INTO qualityproperty VALUES (8001, 'Measurable', 'M');
INSERT INTO qualityproperty VALUES (8002, 'Agreed Upon', 'A');
INSERT INTO qualityproperty VALUES (8003, 'Realistic', 'R');
INSERT INTO qualityproperty VALUES (8004, 'Time-bound', 'T');