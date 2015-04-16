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

INSERT INTO customer VALUES (3000, 'UBS', 'Zürich');

--
-- Data for Name: qacategory; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qacategory VALUES (4000, 'Availability', NULL, NULL);
INSERT INTO qacategory VALUES (4001, 'Sustainability', NULL, NULL);

--
-- Data for Name: qa; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qa VALUES (5000, '<p>Das System soll ______ % verfügbar sein.</p>');
INSERT INTO qa VALUES (5001, '<p>The _________ System is ______ of the year <b>available.</b></p>');