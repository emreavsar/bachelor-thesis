--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO "user" VALUES (-1000, '78c036a9bfc08d0c4150a9b9af9dd61c79ab4b8a', 'admin', 'eATliOMyVQhhmOLINQHO');
INSERT INTO "user" VALUES (-1001, '994b8e7341be4aff15db7412cdafb4e33ac64eb7', 'corina', 'wUScbpcsYHyLjEQTHTmT');
INSERT INTO "user" VALUES (-1002, '4dd3a08cdda45838c11591789b4a47fedc1d3cc9', 'emre.avsar92@gmail.com', 'YqYpSzIoxBmpOliRctnV');
INSERT INTO "user" VALUES (-1003, 'd5506a1c7feda5c81f98f73cb547821d82d04d60', 'ozimmermann', 'zAmVyCdklHphTtkyJuKM');

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO role VALUES (-20000, 'admin');
INSERT INTO role VALUES (-20001, 'dashboard');
INSERT INTO role VALUES (-20002, 'curator');
INSERT INTO role VALUES (-20003, 'analyst');
INSERT INTO role VALUES (-20004, 'synthesizer');
INSERT INTO role VALUES (-20005, 'evaluator');
INSERT INTO role VALUES (-20006, 'projectmanager');

--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO user_role VALUES (-1000, -20000);
INSERT INTO user_role VALUES (-1000, -20001);

--INSERT INTO user_role VALUES (1001, 2000);
INSERT INTO user_role VALUES (-1001, -20001);
INSERT INTO user_role VALUES (-1002, -20000);
INSERT INTO user_role VALUES (-1002, -20001);
INSERT INTO user_role VALUES (-1003, -20000);
INSERT INTO user_role VALUES (-1003, -20001);

--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO customer (id, address, name) VALUES (-3000, 'Zürich', 'UBS');
INSERT INTO customer (id, address, name) VALUES (-3001, 'Rapperswil', 'HSR');

--
-- Data for Name: jiraconnection; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO jiraconnection (id, hostAddress, username, password) VALUES (-1500, 'http://sinv-56055.edu.hsr.ch:8080', 'corina','helloworld');

--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO project (id, jirakey, name, jiraconnection_id, projectcustomer_id) VALUES (-11000, 'QTP', 'Campus Mobile App (iOS)', -1500, -3001);
INSERT INTO project (id, jirakey, name, jiraconnection_id, projectcustomer_id) VALUES (-11001, null, 'Cloud Banking', -1500, -3000);

--
-- Data for Name: favorite_project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO favorite_project (user_id, project_id) VALUES (-1000, -11000);
INSERT INTO favorite_project (user_id, project_id) VALUES (-1000, -11001);
INSERT INTO favorite_project (user_id, project_id) VALUES (-1001, -11001);

--
-- Data for Name: qacategory; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qacategory VALUES (-4000, 'fa fa-cog', 'Functionality', NULL);
INSERT INTO qacategory VALUES (-4001, 'fa fa-heart', 'Usability', NULL);
INSERT INTO qacategory VALUES (-4002, 'fa fa-heart', 'Portability', -4001);
INSERT INTO qacategory VALUES (-4003, 'fa fa-heart', 'Portability2', -4002);
INSERT INTO qacategory VALUES (-4004, 'fa fa-heart', 'Portability3', -4003);
INSERT INTO qacategory VALUES (-4005, 'fa fa-cog', 'Availability', NULL);
INSERT INTO qacategory VALUES (-4006, 'fa fa-cog', 'SubcategoryOfAvailability', -4005);


--
-- Data for Name: catalog; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO catalog (id, description, name, image) VALUES  (-6000, 'Dies ist der Standardkatalog', 'Standard Katalog', NULL);
INSERT INTO catalog (id, description, name, image) VALUES  (-6001, 'Katalog für Cloud Templates', 'Cloud Katalog', NULL);

--
-- Data for Name: qualityproperty; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qualityproperty VALUES (-8000, 'Specific', 'S');
INSERT INTO qualityproperty VALUES (-8001, 'Measurable', 'M');
INSERT INTO qualityproperty VALUES (-8002, 'Agreed Upon', 'A');
INSERT INTO qualityproperty VALUES (-8003, 'Realistic', 'R');
INSERT INTO qualityproperty VALUES (-8004, 'Time-bound', 'T');

--
-- Data for Name: qa; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO qa (id, deleted, description, versionnumber) VALUES (-2000, false, '<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.</p>', 1);
INSERT INTO qa (id, deleted, description, versionnumber) VALUES (-2011, false, '<p>Dieses QA hat einen Range von 0 bis 100 %VARIABLE_FREENUMBER_0% </p>', 1);


-- Data for Name: qacategory_qa; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4005, -2000);

--
-- Data for Name: catalogqa; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7000, false, -6000, -2000);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7012, false, -6000, -2011);

--
-- Data for Name: qavar; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO qavar (id, type, averagevalue, mosteusedvalue, extendable, varindex, template_id) VALUES (-4020, 'FREETEXT', 0, 0, false,  0, -7000);
INSERT INTO qavar (id, type, averagevalue, mosteusedvalue, extendable, varindex, template_id) VALUES (-5020, 'ENUMNUMBER', 0, 0, true, 1, -7000);
INSERT INTO qavar (id, type, averagevalue, mosteusedvalue, extendable, varindex, template_id) VALUES (-6020, 'FREENUMBER', 0, 0, false, 0, -7012);

--
-- Data for Name: qavarval; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6012, false, 'NUMBER', '95', -5020);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6013, false, 'NUMBER', '90', -5020);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6014, true, 'NUMBER', '99', -5020);

--
-- Data for Name: valrange; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO valrange (id, max, min, rangeinvar_id) VALUES (-3314, 100, 0, -6020);


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO task (id, description, done, assignee_id) VALUES (-6010, 'Evaluate Catalog HSR Mobile App', true, -1000);
INSERT INTO task (id, description, done, assignee_id) VALUES (-6011, 'Create Catalog for Cloud Apps', false, -1000);

--
-- Data for Name: instance; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO instance (id, description, project_id, template_id) VALUES (-5, '<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.</p> bad good', -11000, -7000);

--
-- Data for Name: val; Type: TABLE DATA; Schema: public; Owner: qualit
--
