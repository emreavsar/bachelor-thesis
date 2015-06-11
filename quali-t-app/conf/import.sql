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
INSERT INTO projectinitiator (id, address, name) VALUES (-3990, 'Zürich', 'UBS');
INSERT INTO projectinitiator (id, address, name) VALUES (-3991, 'Rapperswil', 'HSR');
INSERT INTO projectinitiator (id, address, name) VALUES (-3992, 'Rapperswil', 'IFS HSR / Prof. Dr. Olaf Zimmermann');

--
-- Data for Name: jiraconnection; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO jiraconnection (id, hostAddress, username, password) VALUES (-1500, 'http://sinv-56055.edu.hsr.ch:8080', 'corina','helloworld');

--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO project (id, jirakey, name, jiraconnection_id, projectinitiator_id) VALUES (-11002, 'QUALI', 'QUALI-T', -1500, -3992);

--
-- Data for Name: favorite_project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO favorite_project (user_id, project_id) VALUES (-1000, -11002);

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

INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2001, false, '<p>Das Frontend und das Backend sollen zwei voneinander soweit unabhängige Komponenten sein, dass diese eigenständig verändert, weiterentwickelt und getestet werden können.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2002, false, '<p>Die komplette Funktionalität von QUALI-T muss in den Browsern <i>%VARIABLE_FREETEXT_0%</i> den Benutzern vollumfänglich zur Verfügung stehen. Unterstützung von <i>%VARIABLE_FREETEXT_1%</i> sind wünschenswert, aber nicht zwingend erforderlich, falls Mehraufwand von über <i>%VARIABLE_ENUMNUMBER_2%</i>h entsteht.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2003, false, '<p>Zur Authentifizierung wird eine User ID mit einem Passwort verwendet. Diese User-Passwörter müssen mindestens <i>%VARIABLE_FREENUMBER_0%</i> Zeichen lang sein, ein Sonderzeichen und Gross-/Kleinschreibung enthalten.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2004, false, '<p>Innerhalb der QUALI-T-Applikation muss der Zugriff auf einzelne Funktionen über ein Rollenkonzept gesichert sein. Es sollen alle fünf Hauptrollen aus Kapitel 2.2.2.1  abgebildet sein. Wer Zugriff auf welche Funktionen hat, lässt sich aus der „Abbildung 6 Use-Case-Diagramm für QUALI-T“ ableiten. Zusätzlich gibt es eine Admin-Rolle, welche uneingeschränkte Berechtigung auf alle Funktionen hat.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2005, false, '<p>Alle Funktionen im <i>%VARIABLE_FREETEXT_0%</i> sollten innerhalb weniger als <i>%VARIABLE_ENUMNUMBER_1%</i>s ausführbar sein, unter Vernachlässigung des Netzwerks (beispielsweise beim lokalen Betrieb).</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2006, false, '<p><i>%VARIABLE_FREETEXT_0%</i> muss mindestens <i>%VARIABLE_FREENUMBER_1%</i> gleichzeitig angemeldete User auf dem Web Client unterstützen.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2007, false, '<p><Das Endprodukt muss ein lauffähiger Prototyp sein, der noch keine Produktivqualitäthat. Der Zusatzaufwand zum Erreichen von Produktivqualität darf den Aufwand der BA jedoch nicht übersteigen.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2008, false, '<p>Nach einer maximalen Einarbeitungszeit von <i>%VARIABLE_ENUMNUMBER_0%</i>h ist der User in der Lage, neue QualityAttributes, Catalogs und Projects zu erstellen. Ausserdem versteht er den Zusammenhang und den Ablauf zwischen diesen drei Programm Modellen.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2009, false, '<p>Das Backend besitzt eine RESTful HTTP API für Backend-Integration aus jedem beliebigen Tool.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2010, false, '<p><span>Die in der Realisierung (der <i>%VARIABLE_FREETEXT_0%</i> Software) verwendeten Tools und Frameworks sind vorzugsweise mit folgenden Lizenzen veröffentlicht: Eclipse, Apache Public (AP), Massachusetts Institute of Technology (MIT) oder Berkeley Software Distribution(BSD).</span></p><p><span>Tools und Frameworks, die mit General Public License (GPL) und Lesser General PublicLicense (LGPL) lizenziert sind, sollten nicht verwendet werden. Die einzige Ausnahme ist das Hibernate Framework (LGPL). Dieses darf nach Absprache mit unserem BA-Betreuer weiterhin verwendet werden.</span></p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2012, false, '<p>Um auch mehrstufige Quality Attribute Trees abbilden zu können, sollen mindestens fünf Hierarchiestufen für die Kategorisierung der QAs möglich sein.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2013, false, '<p>Wenn QA Templates verändert werden, wird in der Datenbank eine neue Version erstellt. Für den Anwender ist jeweils nur die aktuellste Version über QUALI-T aufrufbar. Gibt es aber bereits Instanzen von dem QA Template oder es wird in einem Katalog verwendet, so referenzieren diese QA Templates weiterhin auf die ursprüngliche Version.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2014, false, '<p>REST- und URI-Konzept sind so erstellt, dass Links von und auf andere Applikationen möglich sind. Beispiele für andere Anwendungen sind ADMentor/ADRepo, ART und EEPPI. Es soll eine Web-GUI-Koexistenz möglich sein, jedoch kein Web-Mashup oder Ähnliches.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2015, false, '<p>Alle Datenbankoperationen sollen die ACID-Eigenschaften (Atomicity, Consistency, Integrity, Durability) erfüllen. Während ein Objekt bearbeitet wird, soll dieses in der Datenbank gelockt werden und für andere nicht veränderbar sein. Wird versucht, auf ein gelocktes Objekt zuzugreifen, erhält der Anwender einen Warnhinweis.</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2016, false, '<p>Die Kommunikation zwischen Client und Server Tier ist als vertraulich klassifiziert (kein Mitlesen durch Dritte) und verwendet eine kryptographische Verschlüsselung (kein Manipulieren der Daten).</p>', 1, NULL);
INSERT INTO qa (id, deleted, description, versionnumber, previousversion_id) VALUES (-2017, false, '<p>QUALI-T muss lauffähig mit allen Funktionen auf <i>%VARIABLE_ENUMTEXT_0%</i> deploybar sein.</p>', 1, NULL);

-- Data for Name: qacategory_qa; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4006, -2001);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4031, -2001);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4030, -2001);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4034, -2001);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4002, -2002);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4005, -2003);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4025, -2003);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4003, -2004);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4001, -2005);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4011, -2005);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4001, -2006);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4013, -2006);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4006, -2007);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4003, -2008);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4017, -2008);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4002, -2009);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4015, -2009);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4006, -2010);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4003, -2012);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4003, -2013);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4005, -2013);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4002, -2014);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4014, -2014);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4015, -2014);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4005, -2015);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4026, -2015);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4027, -2015);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4005, -2016);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4025, -2016);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4026, -2016);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4029, -2016);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4007, -2017);
INSERT INTO qacategory_qa (categories_id, usedinqa_id) VALUES (-4036, -2017);

--
-- Data for Name: catalogqa; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7001, false, -6000, -2001);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7002, false, -6000, -2002);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7003, false, -6000, -2003);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7004, false, -6000, -2004);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7005, false, -6000, -2005);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7006, false, -6000, -2006);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7007, false, -6000, -2007);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7008, false, -6000, -2008);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7009, false, -6000, -2009);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7010, false, -6000, -2010);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7011, false, -6000, -2012);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7013, false, -6000, -2013);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7014, false, -6000, -2014);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7015, false, -6000, -2015);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7016, false, -6000, -2016);
INSERT INTO catalogqa (id, deleted, catalog_id, qa_id) VALUES (-7017, false, -6000, -2017);

--
-- Data for Name: qavar; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9003, '5', true, '5', 'ENUMNUMBER', 2, -7002);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9004, NULL, false, NULL, 'FREETEXT', 0, -7002);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9005, NULL, false, NULL, 'FREETEXT', 1, -7002);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9006, '8', false, '8', 'FREENUMBER', 0, -7003);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9007, '1', true, '1', 'ENUMNUMBER', 1, -7005);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9008, NULL, false, NULL, 'FREETEXT', 0, -7005);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9009, '10', false, '10', 'FREENUMBER', 1, -7006);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9010, NULL, false, NULL, 'FREETEXT', 0, -7006);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9011, '3', true, '3', 'ENUMNUMBER', 0, -7008);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9012, NULL, false, NULL, 'FREETEXT', 0, -7010);
INSERT INTO qavar (id, averagevalue, extendable, mosteusedvalue, type, varindex, template_id) VALUES (-9013, 'Heroku', true, 'Heroku', 'ENUMTEXT', 0, -7017);
--
-- Data for Name: qavarval; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6015, false, 'NUMBER', '4', -9003);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6016, true, 'NUMBER', '5', -9003);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6017, false, 'NUMBER', '6', -9003);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6018, false, 'NUMBER', '3', -9007);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6019, false, 'NUMBER', '2', -9007);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6020, true, 'NUMBER', '1', -9007);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6021, false, 'NUMBER', '2', -9011);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6022, true, 'NUMBER', '3', -9011);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6023, false, 'NUMBER', '4', -9011);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6024, false, 'TEXT', 'Heroku', -9013);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6025, false, 'TEXT', 'Microsoft Azure', -9013);
INSERT INTO qavarval (id, isdefault, type, value, valinvar_id) VALUES (-6026, false, 'TEXT', 'Google App Engine', -9013);

--
-- Data for Name: valrange; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO valrange (id, max, min, rangeinvar_id) VALUES (-3315, 10, 4, -9003);
INSERT INTO valrange (id, max, min, rangeinvar_id) VALUES (-3316, 10, 6, -9006);


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO task (id, description, done, assignee_id) VALUES (-6010, 'Evaluate Catalog HSR Mobile App', true, -1000);
INSERT INTO task (id, description, done, assignee_id) VALUES (-6011, 'Create Catalog for Cloud Apps', false, -1000);

--
-- Data for Name: instance; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-10, '<p>Das Endprodukt muss ein lauffähiger Prototyp sein, der noch keine Produktivqualitäthat. Der Zusatzaufwand zum Erreichen von Produktivqualität darf den Aufwand der BA jedoch nicht übersteigen.</p>', NULL, NULL, -11002, -7007);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-11, '<p><i>%VARIABLE_FREETEXT_0%</i> muss mindestens <i>%VARIABLE_FREENUMBER_1%</i> gleichzeitig angemeldete User auf dem Web Client unterstützen.</p>', NULL, NULL, -11002, -7006);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-12, '<p>Das Frontend und das Backend sollen zwei voneinander soweit unabhängige Komponenten sein, dass diese eigenständig verändert, weiterentwickelt und getestet werden können.</p>', NULL, NULL, -11002, -7001);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-13, '<p>QUALI-T muss lauffähig mit allen Funktionen auf <i>%VARIABLE_ENUMTEXT_0%</i> deploybar sein.</p>', NULL, NULL, -11002, -7017);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-14, '<p>Nach einer maximalen Einarbeitungszeit von <i>%VARIABLE_ENUMNUMBER_0%</i>h ist der User in der Lage, neue QualityAttributes, Catalogs und Projects zu erstellen. Ausserdem versteht er den Zusammenhang und den Ablauf zwischen diesen drei Programm Modellen.</p>', NULL, NULL, -11002, -7008);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-15, '<p><span>Die in der Realisierung (der <i>%VARIABLE_FREETEXT_0%</i> Software) verwendeten Tools und Frameworks sind vorzugsweise mit folgenden Lizenzen veröffentlicht: Eclipse, Apache Public (AP), Massachusetts Institute of Technology (MIT) oder Berkeley Software Distribution(BSD).</span></p><p><span>Tools und Frameworks, die mit General Public License (GPL) und Lesser General PublicLicense (LGPL) lizenziert sind, sollten nicht verwendet werden. Die einzige Ausnahme ist das Hibernate Framework (LGPL). Dieses darf nach Absprache mit unserem BA-Betreuer weiterhin verwendet werden.</span></p>', NULL, NULL, -11002, -7010);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-16, '<p>Um auch mehrstufige Quality Attribute Trees abbilden zu können, sollen mindestens fünf Hierarchiestufen für die Kategorisierung der QAs möglich sein.</p>', NULL, NULL, -11002, -7011);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-17, '<p>Die Kommunikation zwischen Client und Server Tier ist als vertraulich klassifiziert (kein Mitlesen durch Dritte) und verwendet eine kryptographische Verschlüsselung (kein Manipulieren der Daten).</p>', NULL, NULL, -11002, -7016);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-18, '<p>Zur Authentifizierung wird eine User ID mit einem Passwort verwendet. Diese User-Passwörter müssen mindestens <i>%VARIABLE_FREENUMBER_0%</i> Zeichen lang sein, ein Sonderzeichen und Gross-/Kleinschreibung enthalten.</p>', NULL, NULL, -11002, -7003);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-19, '<p>Wenn QA Templates verändert werden, wird in der Datenbank eine neue Version erstellt. Für den Anwender ist jeweils nur die aktuellste Version über QUALI-T aufrufbar. Gibt es aber bereits Instanzen von dem QA Template oder es wird in einem Katalog verwendet, so referenzieren diese QA Templates weiterhin auf die ursprüngliche Version.</p>', NULL, NULL, -11002, -7013);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-20, '<p>Innerhalb der QUALI-T-Applikation muss der Zugriff auf einzelne Funktionen über ein Rollenkonzept gesichert sein. Es sollen alle fünf Hauptrollen aus Kapitel 2.2.2.1  abgebildet sein. Wer Zugriff auf welche Funktionen hat, lässt sich aus der Abbildung 6 Use-Case-Diagramm für QUALI-T ableiten. Zusätzlich gibt es eine Admin-Rolle, welche uneingeschränkte Berechtigung auf alle Funktionen hat.</p>', NULL, NULL, -11002, -7004);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-21, '<p>REST- und URI-Konzept sind so erstellt, dass Links von und auf andere Applikationen möglich sind. Beispiele für andere Anwendungen sind ADMentor/ADRepo, ART und EEPPI. Es soll eine Web-GUI-Koexistenz möglich sein, jedoch kein Web-Mashup oder ähnliches.</p>', NULL, NULL, -11002, -7014);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-22, '<p>Alle Datenbankoperationen sollen die ACID-Eigenschaften (Atomicity, Consistency, Integrity, Durability) erfüllen. Während ein Objekt bearbeitet wird, soll dieses in der Datenbank gelockt werden und für andere nicht verÃ¤nderbar sein. Wird versucht, auf ein gelocktes Objekt zuzugreifen, erhält der Anwender einen Warnhinweis.</p>', NULL, NULL, -11002, -7015);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-23, '<p>Die komplette Funktionalität von QUALI-T muss in den Browsern <i>%VARIABLE_FREETEXT_0%</i> den Benutzern vollumfänglich zur Verfügung stehen. Unterstützung von <i>%VARIABLE_FREETEXT_1%</i> sind wünschenswert, aber nicht zwingend erforderlich, falls Mehraufwand von über <i>%VARIABLE_ENUMNUMBER_2%</i>h entsteht.</p>', NULL, NULL, -11002, -7002);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-24, '<p>Das Backend besitzt eine RESTful HTTP API für Backend-Integration aus jedem beliebigen Tool.</p>', NULL, NULL, -11002, -7009);
INSERT INTO instance (id, description, jiradirecturl, jirakey, project_id, template_id) VALUES (-25, '<p>Alle Funktionen im <i>%VARIABLE_FREETEXT_0%</i> sollten innerhalb weniger als <i>%VARIABLE_ENUMNUMBER_1%</i>s ausführbar sein, unter Vernachlässigung des Netzwerks (beispielsweise beim lokalen Betrieb).</p>', NULL, NULL, -11002, -7005);

--
-- Data for Name: val; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO val (id, value, varindex, instance_id) VALUES (-3000, '10', 1, -11);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3001, 'QUALI-T', 0, -11);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3002, '8', 0, -18);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3003, 'Heroku', 0, -13);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3004, 'QUALI-T', 0, -15);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3005, 'Chrome und Firefox', 0, -23);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3006, '5', 2, -23);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3007, 'IE11 und Safari', 1, -23);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3008, '3', 0, -14);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3009, '1', 1, -25);
INSERT INTO val (id, value, varindex, instance_id) VALUES (-3010, 'QUALI-T', 0, -25);

--
-- Data for Name: qualitypropertystatus; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5000, true, -12, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5001, true, -12, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5002, true, -12, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5003, true, -15, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5004, true, -15, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5005, true, -15, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5006, true, -24, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5007, true, -24, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5008, true, -24, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5009, true, -10, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5010, true, -10, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5011, true, -10, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5012, true, -16, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5013, true, -16, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5014, true, -16, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5015, true, -20, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5016, true, -20, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5017, true, -20, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5018, true, -17, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5019, true, -17, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5020, true, -17, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5021, true, -18, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5022, true, -18, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5023, true, -18, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5024, true, -23, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5025, true, -23, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5026, true, -23, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5027, true, -14, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5028, true, -14, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5029, true, -14, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5030, true, -19, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5031, true, -19, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5032, true, -19, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5033, true, -11, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5034, true, -11, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5035, true, -11, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5036, true, -25, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5037, true, -25, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5038, true, -25, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5039, true, -22, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5040, true, -22, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5041, true, -22, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5042, true, -13, -8001);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5043, true, -13, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5044, true, -13, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5045, true, -21, -8003);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5046, true, -21, -8000);
INSERT INTO qualitypropertystatus (id, status, qa_id, qp_id) VALUES (-5047, true, -21, -8001);

--
-- Data for Name: qualityproperty_project; Type: TABLE DATA; Schema: public; Owner: qualit
--

INSERT INTO qualityproperty_project (qualityproperties_id, usedbyproject_id) VALUES (-8000, -11002);
INSERT INTO qualityproperty_project (qualityproperties_id, usedbyproject_id) VALUES (-8001, -11002);
INSERT INTO qualityproperty_project (qualityproperties_id, usedbyproject_id) VALUES (-8003, -11002);