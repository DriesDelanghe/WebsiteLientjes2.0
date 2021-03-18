insert into image(
    id, image_location
)
values
(1, '../images/logo-facebook.svg'),
(2, '../images/logo-instagram.svg'),
(3, '../images/right-arrow.svg'),
(4, '../images/placeholder-personnel-1.jpg'),
(5, '../images/placeholder-personnel-2.jpg'),
(6, '../images/placeholder-personnel-3.jpg'),
(7, '../images/adress-logo.svg'),
(8, '../images/email-logo.svg'),
(9, '../images/phone-call-logo.svg'),
(10, '../images/placeholder-personnel-4.jpg'),
(11, '../images/placeholder-personnel-5.jpg'),
(12, '../images/placeholder-personnel-6.jpg');

insert into personnel(
id, name, image_Id, extra_info, function_description
)
values
(1, 'Persona A', 4, 'This is the first test person, these are all here as proof of concept', 'chef-kok'),
(2, 'Persona B', 5, 'This is the second test person, these are all here as proof of concept', 'serveur'),
(3, 'Persona C', 6, 'This is the third test person, these are all here as proof of concept', 'uitbater'),
(4, 'Persona D', 10, 'This is the fourth test person, these are all here as proof of concept', 'sous-chef'),
(5, 'Persona E', 11, 'This is the fifth test person, these are all here as proof of concept', 'serveur'),
(6, 'Persona F', 12, 'This is the sixth test person, these are all here as proof of concept', 'serveur');

insert into domain(
id, name
)
values
(1, 'bistro');

insert into page(
id, page_name, domain_id
)
values
(1, 'Home', 1),
(2, 'References', 1),
(3, 'Contact', 1),
(4, 'personeel', 1);

insert into personnel_pages(
personnel_id, pages_id
)
values
(1,1),
(2,1),
(3,1),
(1,4),
(2,4),
(3,4),
(4,4),
(5,4),
(6,4);

insert into openingsuur(
id, dag, openingsuur, sluitingsuur, domain_id
)
values
(1, 'maandag', null, null, 1),
(2, 'dinsdag', null, null, 1),
(3, 'woensdag', '10:00', '21:00', 1),
(4, 'donderdag', '10:00', '21:00', 1),
(5, 'vrijdag', '10:00', '21:00', 1),
(6, 'zaterdag', '10:00', '21:00', 1),
(7, 'zondag', '10:00', '21:00', 1);

insert into text_fragment(
id, text_content, order_reference, header_text, page_id
)
values
(1, 'Meer dan een koffiehuis', 1, true, 1),
(2, 'Lientje''s bistro is meer dan enkel een koffiehuis.
                Lientjes bistro is ook een thuishaven voor iedereen wie
                rustig een koffie wilt drinken of wilt genieten van
                kwaliteitvol eten.', 1, false, 1),
(3, 'Het team', 2, true, 1),
(4, 'Het vaste team staat altijd klaar om je te helpen met een glimlach.', 2, false, 1),
(5, 'bekijk het team', 3, false, 1),
(6, 'Bekijk ons menu', 3, true, 1),
(7, 'Naar menu', 4, false, 1),
(8, 'Contact', 4, true, 1),
(9, 'Heb je vragen of wil je reserveren? <br>
                neem dan gerust contact op met ons!', 5, false, 1),
(10, 'Naar contact', 6, false, 1),
(11, 'Openingsuren', 5, true, 1),
(12, 'Onze sociale media', 6, true, 1),
(13, 'Bezoek ook onze zusterzaak', 7, true, 1),
(14, 'naar Bolo lientje''s', 6, false, 1),
(15, 'onze contactgegevens', 1, true, 3),
(16, 'onze sociale media', 2, true, 3);

insert into symbol(
id, image_id, reference_name
)
values
(1, 3, 'rightArrow');

insert into social_media(
id, social_media_url, image_id, order_reference, domain_id
)
values
(1, 'https://www.facebook.com/Lientjes-Bistro-151107858896317/', 1, 1, 1),
(2, 'https://www.instagram.com/lientjesbistro/', 2, 2, 1);

insert into reference(
id, site_url, site_name, artist_name, artist_url, product_name
)
values
(1, 'https://www.flaticon.com/', 'Flaticon', 'Roundicons', 'https://roundicons.com/', 'symbols'),
(2, 'https://www.pexels.com/', 'Pexels', 'Negative Space', 'https://www.pexels.com/@negativespace', 'images'),
(3, null, null, 'Dries Delanghe', 'https://www.driesdelanghe.be/', 'site');

insert into contact_info(
id, info_name, info_content, domain_id, image_id
)
values
(1, 'email', 'info@lientjes-coffeebreak.be', 1, 8),
(2, 'adres', 'Stationstraat 143, 2845 Niel', 1, 7),
(3, 'telefoon', '03 344 85 79', 1, 9);