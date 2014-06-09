use group3;

SET autocommit=0;
BEGIN;

INSERT INTO password (salt_id, hashedValue) VALUES (hex(169), '8D8BDE55A29E3BE67EA7E69E7AFDA78F');                               #john_21      =  Arin@j0y
SELECT @password.id := max(password_id) FROM password;
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+1, hex(156), '7B9709386FBEC85ADD999164330CCF9B');  #david25      =  Hahah3!45
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+2, hex(156), '7B9709386FBEC85ADD999164330CCF9B');  #andrew_23    =  Hahah3!45
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+3, hex(156), '7B9709386FBEC85ADD999164330CCF9B');  #harry123     =  Hahah3!45
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+4, hex(169), '8D8BDE55A29E3BE67EA7E69E7AFDA78F');  #monash123    =  Arin@j0y
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+5, hex(156), '7B9709386FBEC85ADD999164330CCF9B');  #tommy123     =  Hahah3!45
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+6, hex(156), '7B9709386FBEC85ADD999164330CCF9B');  #peter123     =  Hahah3!45
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+7, hex(234), '8AE1DD156C62F4F0B0B31C29B46F8E48');  #lawrence123  =  P4ssw0rd!
INSERT INTO password (password_id, salt_id, hashedValue) VALUES (@password.id+8, hex(234), '8AE1DD156C62F4F0B0B31C29B46F8E48');  #arinjoy123   =  P4ssw0rd!

INSERT INTO person (firstName, lastName, email, username, password) VALUES ('John', 'Smith', 'john.smith@yahoo.com', 'john_21', @password.id+0);
SELECT @person.id := max(person_id) FROM person;
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+1, 'David', 'Jones', 'david.jj@hotmail.com', 'david25', @password.id+1);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+2, 'Andrew', 'Paul', 'andrew.p@monash.edu', 'andrew_23', @password.id+2);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+3, 'Harry', 'Kenton', 'harry_hero@gmail.com', 'harry123', @password.id+3);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+4, 'John', 'Monash', 'john.monash@monash.ecu', 'monash123', @password.id+4);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+5, 'Tom', 'Cruise', 'tom.cruise@gmailc.com', 'tommy123', @password.id+5);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+6, 'Peter', 'Davis', 'peter@hotmail.com', 'peter123', @password.id+6);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+7, 'Lawrence', 'Colman', 'lpcol2@studnet.monash.edu', 'lawrence123', @password.id+7);
INSERT INTO person (person_id, firstName, lastName, email, username, password) VALUES (@person.id+8, 'Arinjoy', 'Biswas', 'abis8@student.monash.ecu', 'arinjoy123', @password.id+8);

INSERT INTO location (floor, streetNo, street, landmark, town, region, postcode, country) VALUES ('2nd', '23', 'Alma Road', 'Near Theatre', 'Prahran', 'Victoria', '3344', 'Australia');
SELECT @location.id := max(location_id) FROM location;
INSERT INTO location (location_id, floor, streetNo, street, landmark, town, region, postcode, country) VALUES (@location.id+1, 'H22', '321', 'Dandenong Road', 'Racecourse', 'Melbourne', 'Victoria', '3211', 'Asutralia');

INSERT INTO mission (name, description, time, captain_id, location_id) VALUES ('Great Mission', 'You will never forget this adventure in your lifetime', '2015-01-23 22:21:00', @person.id+0, @location.id);
SELECT @mission.id := max(mission_id) FROM mission;
INSERT INTO mission (mission_id, name, description, time, captain_id, location_id) VALUES (@mission.id+1, 'Niceest Mission', 'Please come and get thrilled with this adventure of your life', '2015-03-22 22:21:00', @person.id+0, @location.id+1);
INSERT INTO mission (mission_id, name, description, time, captain_id, location_id) VALUES (@mission.id+2, 'Mars Xplorer', 'This is the best mission of all the missionsin the world', '2015-05-12 22:21:00', @person.id+1, @location.id+0);

INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+1, @mission.id+0);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+2, @mission.id+0);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+2, @mission.id+1);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+3, @mission.id+1);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+3, @mission.id+2);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+4, @mission.id+0);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+4, @mission.id+1);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+4, @mission.id+2);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+5, @mission.id+0);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+5, @mission.id+1);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+6, @mission.id+0);
INSERT INTO person_mission (person_id, mission_id) VALUES (@person.id+7, @mission.id+2);

INSERT INTO invitation (lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54', @person.id+0, @person.id+1, @mission.id+0);
SELECT @invitation.id := max(invitation_id) FROM invitation;
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+1, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+2, @mission.id+0);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+2, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+4, @mission.id+0);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+3, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+5, @mission.id+0);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+4, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+6, @mission.id+0);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+5, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+2, @mission.id+1);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+6, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+3, @mission.id+1);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+7, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+4, @mission.id+1);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+8, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+0, @person.id+5, @mission.id+1);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+9, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54',  @person.id+1, @person.id+3, @mission.id+2);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+10, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54', @person.id+1, @person.id+4, @mission.id+2);
INSERT INTO invitation (invitation_id, lastUpdated, status, creator_id, recipient_id, mission_id) VALUES (@invitation.id+11, NOW(), X'ACED00057E72003B666C796D65746F6D6172732E636F6D6D6F6E2E646174617472616E736665722E496E7669746174696F6E24496E7669746174696F6E53746174757300000000000000001200007872000E6A6176612E6C616E672E456E756D0000000000000000120000787074000453454E54', @person.id+1, @person.id+7, @mission.id+2);

COMMIT;
SET autocommit=1;
