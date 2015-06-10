--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO "user" VALUES (-1000, '78c036a9bfc08d0c4150a9b9af9dd61c79ab4b8a', 'admin@quali-t.ch', 'eATliOMyVQhhmOLINQHO');

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO role VALUES (-20000, 'admin');
INSERT INTO role VALUES (-20001, 'curator');
INSERT INTO role VALUES (-20002, 'analyst');
INSERT INTO role VALUES (-20003, 'synthesizer');
INSERT INTO role VALUES (-20004, 'evaluator');
INSERT INTO role VALUES (-20005, 'projectmanager');

--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO user_role VALUES (-1000, -20000);
INSERT INTO user_role VALUES (-1000, -20001);
INSERT INTO user_role VALUES (-1000, -20002);
INSERT INTO user_role VALUES (-1000, -20003);
INSERT INTO user_role VALUES (-1000, -20004);
INSERT INTO user_role VALUES (-1000, -20005);

--
-- Data for Name: projectInitiator; Type: TABLE DATA; Schema: public; Owner: qualit
--
INSERT INTO projectinitiator (id, address, name) VALUES (-3000, 'Zürich', 'UBS');
INSERT INTO projectinitiator (id, address, name) VALUES (-3001, 'Rapperswil', 'HSR');

--
-- Data for Name: jiraconnection; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO jiraconnection (id, hostAddress, username, password) VALUES (-1500, 'http://sinv-56055.edu.hsr.ch:8080', 'corina','helloworld');

--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO project (id, jirakey, name, jiraconnection_id, projectInitiator_id) VALUES (-11000, 'QTP', 'Campus Mobile App (iOS)', -1500, -3001);
INSERT INTO project (id, jirakey, name, jiraconnection_id, projectInitiator_id) VALUES (-11001, null, 'Cloud Banking', -1500, -3000);

--
-- Data for Name: favorite_project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO favorite_project (user_id, project_id) VALUES (-1000, -11000);
INSERT INTO favorite_project (user_id, project_id) VALUES (-1000, -11001);

--
-- Data for Name: qacategory; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qacategory VALUES (-4000, 'fa fa-cog', 'Functional Suitability', NULL);
INSERT INTO qacategory VALUES (-4001, 'fa fa-fighter-jet', 'Performance Efficiency', NULL);
INSERT INTO qacategory VALUES (-4002, 'fa fa-plug', 'Compatibility', NULL);
INSERT INTO qacategory VALUES (-4003, 'fa fa-heart', 'Usability', NULL);
INSERT INTO qacategory VALUES (-4004, 'fa fa-bullseye', 'Reliability', NULL);
INSERT INTO qacategory VALUES (-4005, 'fa fa-lock', 'Security', NULL);
INSERT INTO qacategory VALUES (-4006, 'fa fa-wrench', 'Maintainability', NULL);
INSERT INTO qacategory VALUES (-4007, 'fa fa-share-square-o', 'Portability', NULL);
INSERT INTO qacategory VALUES (-4008, '', 'Functional Completeness', -4000);
INSERT INTO qacategory VALUES (-4009, '', 'Functional Correctness', -4000);
INSERT INTO qacategory VALUES (-4010, '', 'Functional Appropriateness', -4000);
INSERT INTO qacategory VALUES (-4011, '', 'Time Behaviour', -4001);
INSERT INTO qacategory VALUES (-4012, '', 'Resource Utilization', -4001);
INSERT INTO qacategory VALUES (-4013, '', 'Capacity', -4001);
INSERT INTO qacategory VALUES (-4014, '', 'Co-existence', -4002);
INSERT INTO qacategory VALUES (-4015, '', 'Interoperability', -4002);
INSERT INTO qacategory VALUES (-4016, '', 'Appropriateness Recognizability', -4003);
INSERT INTO qacategory VALUES (-4017, '', 'Learnability', -4003);
INSERT INTO qacategory VALUES (-4018, '', 'Operability', -4003);
INSERT INTO qacategory VALUES (-4019, '', 'User Error Protection', -4003);
INSERT INTO qacategory VALUES (-4020, '', 'User Interface Aesthetics', -4003);
INSERT INTO qacategory VALUES (-4021, '', 'Maturity', -4004);
INSERT INTO qacategory VALUES (-4022, '', 'Availability', -4004);
INSERT INTO qacategory VALUES (-4023, '', 'Fault Tolerance', -4004);
INSERT INTO qacategory VALUES (-4024, '', 'Recoverability', -4004);
INSERT INTO qacategory VALUES (-4025, '', 'Confidentiality', -4005);
INSERT INTO qacategory VALUES (-4026, '', 'Integrity', -4005);
INSERT INTO qacategory VALUES (-4027, '', 'Non-repudiation', -4005);
INSERT INTO qacategory VALUES (-4028, '', 'Accountability', -4005);
INSERT INTO qacategory VALUES (-4029, '', 'Authenticity', -4005);
INSERT INTO qacategory VALUES (-4030, '', 'Modularity', -4006);
INSERT INTO qacategory VALUES (-4031, '', 'Reusability', -4006);
INSERT INTO qacategory VALUES (-4032, '', 'Analysability', -4006);
INSERT INTO qacategory VALUES (-4034, '', 'Modifiability', -4006);
INSERT INTO qacategory VALUES (-4035, '', 'Testability', -4006);
INSERT INTO qacategory VALUES (-4036, '', 'Adaptability', -4007);
INSERT INTO qacategory VALUES (-4037, '', 'Installability', -4007);
INSERT INTO qacategory VALUES (-4038, '', 'Replaceability', -4007);

--
-- Data for Name: catalog; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO catalog (id, description, name, image) VALUES  (-6000, 'The standard catalog includes all Quality Attribute Templates', 'Standard Catalog', NULL);

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

--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO task (id, description, done, assignee_id) VALUES (-6010, 'Evaluate Catalog HSR Mobile App', true, -1000);
INSERT INTO task (id, description, done, assignee_id) VALUES (-6011, 'Create Catalog for Cloud Apps', false, -1000);

--
-- Data for Name: instance; Type: TABLE DATA; Schema: public; Owner: qualit
--

--
-- Data for Name: val; Type: TABLE DATA; Schema: public; Owner: qualit
--
