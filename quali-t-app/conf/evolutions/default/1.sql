# --- !Ups

INSERT INTO public."user" (id, hashedpassword, name, salt) VALUES (1000, '78c036a9bfc08d0c4150a9b9af9dd61c79ab4b8a', 'admin', 'eATliOMyVQhhmOLINQHO');
INSERT INTO public."user" (id, hashedpassword, name, salt) VALUES (1001, '994b8e7341be4aff15db7412cdafb4e33ac64eb7', 'corina', 'wUScbpcsYHyLjEQTHTmT');
INSERT INTO public."user" (id, hashedpassword, name, salt) VALUES (1002, '4dd3a08cdda45838c11591789b4a47fedc1d3cc9', 'emre', 'YqYpSzIoxBmpOliRctnV');
INSERT INTO public."user" (id, hashedpassword, name, salt) VALUES (1003, 'd5506a1c7feda5c81f98f73cb547821d82d04d60', 'ozimmermann', 'zAmVyCdklHphTtkyJuKM');

INSERT INTO public.role (id, name) VALUES (2000, 'admin');
INSERT INTO public.role (id, name) VALUES (2001, 'crudDashboard');

INSERT INTO public.role_user (user_id, roles_id) VALUES (1000, 2000);
INSERT INTO public.role_user (user_id, roles_id) VALUES (1000, 2001);

INSERT INTO public.role_user (user_id, roles_id) VALUES (1001, 2001);
INSERT INTO public.role_user (user_id, roles_id) VALUES (1002, 2001);

# --- !Downs

delete from public.user_role;
delete from public."user";
delete from public.role;