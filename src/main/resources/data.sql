insert into image(id, image_location)
values (nextval('IMG_SEQ'), '/images/general/default-image.jpg'),
       (nextval('IMG_SEQ'), '/images/general/pexels-negative-space-134577.jpg'),
       (nextval('IMG_SEQ'), '/images/socialmedia/logo-facebook.svg'),
       (nextval('IMG_SEQ'), '/images/socialmedia/logo-instagram.svg'),
       (nextval('IMG_SEQ'), '/images/general/right-arrow.svg'),
       (nextval('IMG_SEQ'), '/images/contactinfo/adress-logo.svg'),
       (nextval('IMG_SEQ'), '/images/contactinfo/email-logo.svg'),
       (nextval('IMG_SEQ'), '/images/contactinfo/phone-call-logo.svg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-1.jpg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-2.jpg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-3.jpg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-4.jpg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-5.jpg'),
       (nextval('IMG_SEQ'), '/images/personnel/placeholder-personnel-6.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-croque.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-dessert.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-hoofdgerecht.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-koude-drank.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-ontbijt.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-warme-drank.jpg'),
       (nextval('IMG_SEQ'), '/images/food/afbeelding-alle-gerechten.jpg');

insert into domain(id, domain_name)
values (1, 'bistro'),
       (2, 'bolo');

insert into personnel(id, name, image_Id, extra_info, function_description, domain_id)
values (nextval('PER_SEQ'), 'Persona A', 9, 'This is the first test person, these are all here as proof of concept',
        'chef-kok', 1),
       (nextval('PER_SEQ'), 'Persona B', 10, 'This is the second test person, these are all here as proof of concept',
        'serveur', 1),
       (nextval('PER_SEQ'), 'Persona C', 11, 'This is the third test person, these are all here as proof of concept',
        'uitbater', 1),
       (nextval('PER_SEQ'), 'Persona D', 12, 'This is the fourth test person, these are all here as proof of concept',
        'sous-chef', 1),
       (nextval('PER_SEQ'), 'Persona E', 13, 'This is the fifth test person, these are all here as proof of concept',
        'serveur', 1),
       (nextval('PER_SEQ'), 'Persona F', 14, 'This is the sixth test person, these are all here as proof of concept',
        'serveur', 1),
       (nextval('PER_SEQ'), 'Persona A', 9, 'This is the first test person, these are all here as proof of concept',
        'chef-kok', 2),
       (nextval('PER_SEQ'), 'Persona B', 10, 'This is the second test person, these are all here as proof of concept',
        'serveur', 2),
       (nextval('PER_SEQ'), 'Persona C', 11, 'This is the third test person, these are all here as proof of concept',
        'uitbater', 2);

insert into page(id, page_name, domain_id, image_id)
values (1, 'home', 1, 2),
       (2, 'references', 1, 2),
       (3, 'contact', 1, 2),
       (4, 'personeel', 1, 2),
       (5, 'menu', 1, 2),
       (6, 'home', 2, null);

insert into personnel_pages(personnel_id, pages_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (1, 4),
       (2, 4),
       (3, 4),
       (4, 4),
       (5, 4),
       (6, 4),
       (7, 6),
       (8, 6),
       (9, 6);

insert into openingsuur(id, dag, openingsuur, sluitingsuur, domain_id)
values (1, 'maandag', null, null, 1),
       (2, 'dinsdag', null, null, 1),
       (3, 'woensdag', '10:00', '21:00', 1),
       (4, 'donderdag', '10:00', '21:00', 1),
       (5, 'vrijdag', '10:00', '21:00', 1),
       (6, 'zaterdag', '10:00', '21:00', 1),
       (7, 'zondag', '10:00', '21:00', 1);

insert into text_fragment(id, text_content, order_reference, header_text, page_id)
values (1, 'Meer dan een koffiehuis', 1, true, 1),
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
       (16, 'onze sociale media', 2, true, 3),
       (17, 'contacteer ons hier', 3, true, 3),
       (18, 'Je naam:', 2, false, 3),
       (19, 'Je email:', 3, false, 3),
       (20, 'soort vraag:', 4, false, 3),
       (21, 'algemene info', 4, true, 3),
       (22, 'onderwerp:', 5, false, 3),
       (23, 'Je vraag', 5, true, 3),
       (24, 'Je vraag is verstuurd!', 6, true, 3),
       (25, 'Gelieve alle velden in te vullen', 5, false, 3),
       (26, 'Vind makkelijk je weg naar ons!', 1, false, 3);

insert into symbol(id, image_id, reference_name)
values (1, 5, 'rightArrow');

insert into social_media(id, social_media_url, image_id, order_reference, domain_id)
values (nextval('sm_seq'), 'https://www.facebook.com/Lientjes-Bistro-151107858896317/', 3, 1, 1),
       (nextval('sm_seq'), 'https://www.instagram.com/lientjesbistro/', 4, 2, 1);

insert into reference(id, site_url, site_name, artist_name, artist_url, product_name)
values (nextval('REF_SEQ'), 'https://www.flaticon.com/', 'Flaticon', 'Roundicons', 'https://roundicons.com/',
        'symbols'),
       (nextval('REF_SEQ'), 'https://www.pexels.com/', 'Pexels', 'Negative Space',
        'https://www.pexels.com/@negativespace',
        'header image'),
       (nextval('REF_SEQ'), null, null, 'Dries Delanghe', 'https://www.driesdelanghe.be/', 'site'),
       (nextval('REF_SEQ'), 'https://www.pexels.com/', 'Pexels', 'Amine M''Siouri',
        'https://www.pexels.com/nl-nl/@amine-m-siouri-1025778',
        'error page header');

insert into contact_info(id, info_name, info_content, domain_id, image_id)
values (nextval('ci_seq'), 'email', 'info@lientjes-coffeebreak.be', 1, 7),
       (nextval('ci_seq'), 'adres', 'Stationstraat 143, 2845 Niel', 1, 6),
       (nextval('ci_seq'), 'telefoon', '03 344 85 79', 1, 8);

insert into menu_section(id, domain_id, name, image_id)
values (nextval('MSEC_SEQ'), 1, 'Alle producten', 21),
       (nextval('MSEC_SEQ'), 2, 'Alle producten', 1),
       (nextval('MSEC_SEQ'), 1, 'Warme Dranken', 20),
       (nextval('MSEC_SEQ'), 1, 'Koude Dranken', 18),
       (nextval('MSEC_SEQ'), 1, 'Ontbijt', 20),
       (nextval('MSEC_SEQ'), 1, 'Versnaperingen', 15),
       (nextval('MSEC_SEQ'), 1, 'Gerechten', 17),
       (nextval('MSEC_SEQ'), 1, 'Desserten', 16),
       (nextval('MSEC_SEQ'), 2, 'Pasta''s', 1);

insert into menu_sub_section(id, name, extra_info)
values (nextval('MSS_SEQ'), 'Koffie', null),
       (nextval('MSS_SEQ'), 'Speciale Koffieskes', null),
       (nextval('MSS_SEQ'), 'Koffie met Alcohol', null),
       (nextval('MSS_SEQ'), 'Pure leaf', null),
       (nextval('MSS_SEQ'), 'Zakskes Thee', null),
       (nextval('MSS_SEQ'), 'Warme Melk', null),
       (nextval('MSS_SEQ'), 'Warme Choco', null),
       (nextval('MSS_SEQ'), 'Fris', null),
       (nextval('MSS_SEQ'), 'Streekbier', null),
       (nextval('MSS_SEQ'), 'Bieren op Vat', null),
       (nextval('MSS_SEQ'), 'Bieren op Fles', null),
       (nextval('MSS_SEQ'), 'Wijnen', 'Huiswijn wit of rood'),
       (nextval('MSS_SEQ'), 'Cava', null),
       (nextval('MSS_SEQ'), 'Aperitief', null),
       (nextval('MSS_SEQ'), 'Alcohol', null),
       (nextval('MSS_SEQ'), 'Ontbijten tot 11u30', 'Vanaf zes personen wordt er roerei bij het ontbijt geserveerd'),
       (nextval('MSS_SEQ'), 'Borrelhapjes', null),
       (nextval('MSS_SEQ'), 'Dagmenu tot 17u00', null),
       (nextval('MSS_SEQ'), 'Kleine honger tot 17u00', null),
       (nextval('MSS_SEQ'), 'Starters', null),
       (nextval('MSS_SEQ'), 'Hoofdgerechten', null),
       (nextval('MSS_SEQ'), 'Salades', null),
       (nextval('MSS_SEQ'), 'Pasta', null),
       (nextval('MSS_SEQ'), 'Kindermenu', null),
       (nextval('MSS_SEQ'), 'Nagerechten', null),
       (nextval('MSS_SEQ'), 'Pannenkoeken & Wafels', null),
       (nextval('MSS_SEQ'), 'Bevrozen genot', null),
       (nextval('MSS_SEQ'), 'pastas', null),
       (nextval('MSS_SEQ'), 'Kindermenu', null);

insert into product(id, menu_sub_section_id, name, price_in_eur, extra_info)
values (nextval('PROD_SEQ'), 1, 'Lungo', 2.10, null),
       (nextval('PROD_SEQ'), 1, 'Americano', 2.30, null),
       (nextval('PROD_SEQ'), 1, 'Potje koffie', 4.00, null),
       (nextval('PROD_SEQ'), 1, 'Espresso', 2.10, null),
       (nextval('PROD_SEQ'), 1, 'Dubbele espresso', 2.60, null),
       (nextval('PROD_SEQ'), 2, 'Chai Latte', 3.30, null),
       (nextval('PROD_SEQ'), 2, 'White Mocha', 3.90, null),
       (nextval('PROD_SEQ'), 2, 'Latte Vanille', 3.60, null),
       (nextval('PROD_SEQ'), 2, 'Latte Hazelnoot', 3.60, null),
       (nextval('PROD_SEQ'), 2, 'Latte Speculoos', 3.60, null),
       (nextval('PROD_SEQ'), 3, 'Yellow Coffee', 4.90, null),
       (nextval('PROD_SEQ'), 3, 'Irish Coffee', 7.10, null),
       (nextval('PROD_SEQ'), 3, 'Italian Coffee', 7.10, null),
       (nextval('PROD_SEQ'), 3, 'French Coffee', 7.10, null),
       (nextval('PROD_SEQ'), 3, 'Hasseltse Koffie', 7.10, null),
       (nextval('PROD_SEQ'), 4, 'Thee infuus', 2.80, null),
       (nextval('PROD_SEQ'), 4, 'Tea for two', 5.00, null),
       (nextval('PROD_SEQ'), 5, 'Rosenbottelthee', 2.10, null),
       (nextval('PROD_SEQ'), 6, 'Warme verse Melk', 2.50, null),
       (nextval('PROD_SEQ'), 6, 'Met extra smaakje', 0.90,
        'Smaakjes vriij te kiezen: <br> hazelnoot - vanille - caramel - speculaas - kaneel'),
       (nextval('PROD_SEQ'), 7, 'Warme verse Melk met echte Chocolade Callets van Callebaut', 3.50,
        'Keuze uit pire - melk of witte korrels'),
       (nextval('PROD_SEQ'), 7, 'Met extra smaakje', 0.90,
        'Smaakjes vriij te kiezen: <br> hazelnoot - vanille - caramel - speculaas - kaneel'),
       (nextval('PROD_SEQ'), 8, 'Bru plat', 2.20, null),
       (nextval('PROD_SEQ'), 8, 'Bru plat 0,5l', 4.10, null),
       (nextval('PROD_SEQ'), 8, 'Bru bruis', 2.20, null),
       (nextval('PROD_SEQ'), 8, 'Bru bruis 0,5l', 4.10, null),
       (nextval('PROD_SEQ'), 8, 'Coca Cola Regular', 2.20, null),
       (nextval('PROD_SEQ'), 9, 'Hellegat blond', 3.00, null),
       (nextval('PROD_SEQ'), 9, 'Hellegat bruin', 3.00, null),
       (nextval('PROD_SEQ'), 9, 'Rupeliaan', 3.80, null),
       (nextval('PROD_SEQ'), 10, 'Stella 25cl', 2.30, null),
       (nextval('PROD_SEQ'), 10, 'Stella 33cl', 2.90, null),
       (nextval('PROD_SEQ'), 10, 'Leffe bruin', 3.60, null),
       (nextval('PROD_SEQ'), 10, 'La Chouffe', 3.60, null),
       (nextval('PROD_SEQ'), 10, 'Tripel d''Anvers', 3.60, null),
       (nextval('PROD_SEQ'), 11, 'Jupiler', 2.50, null),
       (nextval('PROD_SEQ'), 11, 'Vedett', 2.80, null),
       (nextval('PROD_SEQ'), 11, 'Hoegaarden', 2.50, null),
       (nextval('PROD_SEQ'), 11, 'Hoegaarden Rosé', 2.50, null),
       (nextval('PROD_SEQ'), 11, 'Kriek Liefmans', 2.50, null),
       (nextval('PROD_SEQ'), 12, 'Glas', 3.50, null),
       (nextval('PROD_SEQ'), 12, 'Karaf 0,5l', 10.00, null),
       (nextval('PROD_SEQ'), 12, 'wijn 0,75l', 15.00, null),
       (nextval('PROD_SEQ'), 13, 'Glas cava', 4.50, null),
       (nextval('PROD_SEQ'), 13, 'Cava 0,75l', 18.00, null),
       (nextval('PROD_SEQ'), 14, 'Lientje''s aperitief', 4.00, null),
       (nextval('PROD_SEQ'), 14, 'Porto rood of wit', 4.00, null),
       (nextval('PROD_SEQ'), 14, 'Sherry Pedro Domecq', 4.00, null),
       (nextval('PROD_SEQ'), 14, 'Martini Blanco of Rosso', 4.00, null),
       (nextval('PROD_SEQ'), 15, 'Bacardi', 6.00, null),
       (nextval('PROD_SEQ'), 15, 'Jack Daniels', 6.00, null),
       (nextval('PROD_SEQ'), 15, 'Amaretto', 4.00, null),
       (nextval('PROD_SEQ'), 15, 'Bombay Gin', 6.00, null),
       (nextval('PROD_SEQ'), 15, 'Eristoff Vodka', 6.00, null),
       (nextval('PROD_SEQ'), 16, 'Klassiek ontbijt', 11.00,
        'Vers fruitsap, 1 koffie of thee, assortiment van broodjes, mini croissant, mini koffiekoekjes, yoghurt , kaas, ham, confituur, choco, speculaaspasta, 1 eitje naar keuze'),
       (nextval('PROD_SEQ'), 16, 'Luxe-ontbijt', 5.00,
        'Vers fruitsap, 1 koffie of thee, assortiment van broodjes, yoghurt , mini croissant, mini koffiekoekjes, uitgebreider assortiment van zoet en hartig beleg met Franse kaas, 1 eitje naar keuze'),
       (nextval('PROD_SEQ'), 16, ' Kinderontbijtje', 7.00,
        'Verse fruitsap of fristi of chocolademelk, chocoladebroodje, sandwich, confituur, choco & speculaaspasta, verrassing'),
       (nextval('PROD_SEQ'), 16, 'Cava', 21.50,
        'Vers fruitsap, glas cava met Scottish toast (gerookte zalm en roereitje) assortiment van zoet en hartig beleg, Franse kaas, 1 koffie of thee, assortiment van broodjes, croissant, koffiekoekjes.'),
       (nextval('PROD_SEQ'), 17, 'Portie kaas of salami', 5.50, null),
       (nextval('PROD_SEQ'), 17, 'Portie gemend', 8.00, null),
       (nextval('PROD_SEQ'), 17, 'Portie warme hapjes', 12.00, null),
       (nextval('PROD_SEQ'), 17, 'Tapasplanke', 15.00, null),
       (nextval('PROD_SEQ'), 17, 'Breekbrood', 11.00, null),
       (nextval('PROD_SEQ'), 18, 'Dagelijkse aanbieding met soep en hoofdgerecht', 14.95, null),
       (nextval('PROD_SEQ'), 19, 'Boterham met ham of kaas', 8.00, null),
       (nextval('PROD_SEQ'), 19, 'Boterham jam en kaas', 10.00, null),
       (nextval('PROD_SEQ'), 19, 'Veggie sandwich', 8.00, null),
       (nextval('PROD_SEQ'), 19, 'Omelet of spiegelei', 8.00, null),
       (nextval('PROD_SEQ'), 19, 'Omelet champignon', 9.50, null),
       (nextval('PROD_SEQ'), 20, 'Hartige dagsoep', 5.50, null),
       (nextval('PROD_SEQ'), 20, 'Rundscarpaccio', 12.00, null),
       (nextval('PROD_SEQ'), 20, 'Kaas Kroketten 2st. met garni.', 12.00, null),
       (nextval('PROD_SEQ'), 20, 'Garnaal kroketten 2st. met garni.', 15.50, null),
       (nextval('PROD_SEQ'), 20, 'Duo van kaas en garnaal kroketten', 13.50, null),
       (nextval('PROD_SEQ'), 21, 'Vidé huisgemaakt', 15.00, null),
       (nextval('PROD_SEQ'), 21, 'Soep van Trien den heilige', 15.00, null),
       (nextval('PROD_SEQ'), 21, 'Witloof in den oven', 15.00, null),
       (nextval('PROD_SEQ'), 21, 'Steak met poivre vert of archiduc saus', 19.00, 'Geserveerd met koude garnituur'),
       (nextval('PROD_SEQ'), 21, 'Kipfilet met poivre vert of archiduc saus', 16.00, 'Geserveerd met koude garnituur'),
       (nextval('PROD_SEQ'), 22, 'Salade niçoise met tonijn', 17.00, null),
       (nextval('PROD_SEQ'), 22, 'Scampi salade (6st)', 18.00, null),
       (nextval('PROD_SEQ'), 22, 'Sla met geitenkaas, spekjes en honingdressing', 17.00, null),
       (nextval('PROD_SEQ'), 23, 'Bololientjes', 14.00, null),
       (nextval('PROD_SEQ'), 23, 'Carbonara', 14.00, null),
       (nextval('PROD_SEQ'), 23, 'Veggie', 14.00, null),
       (nextval('PROD_SEQ'), 23, 'Scampi (6st) met pikante roomsaus', 19.00, null),
       (nextval('PROD_SEQ'), 24, 'Kids Vidé', 8.50, null),
       (nextval('PROD_SEQ'), 24, 'Kids schep van heilige Trien', 8.50, null),
       (nextval('PROD_SEQ'), 24, 'Kids Bolo', 8.50, null),
       (nextval('PROD_SEQ'), 24, 'Croque uit het vuistje', 7.00, null),
       (nextval('PROD_SEQ'), 24, 'Kids friet met mayo of ketchup', 4.50, null),
       (nextval('PROD_SEQ'), 25, 'Gebak van de dag', 5.00, null),
       (nextval('PROD_SEQ'), 25, 'Gebak van de dag + koffie', 6.50, null),
       (nextval('PROD_SEQ'), 25, 'Appelstrüdel met ijs en slagroom', 9.00, null),
       (nextval('PROD_SEQ'), 25, 'Panacota', 10.00, null),
       (nextval('PROD_SEQ'), 25, 'Moelleux au chocolat', 12.00, null),
       (nextval('PROD_SEQ'), 26, 'Pannekoek', 5.50, null),
       (nextval('PROD_SEQ'), 26, 'Pan banan', 12.00, null),
       (nextval('PROD_SEQ'), 26, 'Pannenkoek met vanilla ijs en warme choco', 8.50, null),
       (nextval('PROD_SEQ'), 26, 'Normandische pannenkoek', 14.50, null),
       (nextval('PROD_SEQ'), 26, 'Wafel', 5.50, null),
       (nextval('PROD_SEQ'), 27, 'Coupe vanille', 7.50, null),
       (nextval('PROD_SEQ'), 27, 'Bananasplit', 9.00, null),
       (nextval('PROD_SEQ'), 27, 'Dame Blanche', 8.00, null),
       (nextval('PROD_SEQ'), 27, 'Coupe aardbei (enkel seizoen)', 10.00, null),
       (nextval('PROD_SEQ'), 27, 'Coupe vers fruit', 10.00, null),
       (nextval('PROD_SEQ'), 28, 'Bololientjes', 14.00, null),
       (nextval('PROD_SEQ'), 29, 'Kids Bolo', 8.50, null);

insert into menu_section_menu_sub_section_list(menu_sub_section_list_id, menu_section_list_id)
values (1, 3),
       (2, 3),
       (3, 3),
       (4, 3),
       (5, 3),
       (6, 3),
       (7, 3),
       (8, 4),
       (9, 4),
       (10, 4),
       (11, 4),
       (12, 4),
       (13, 4),
       (14, 4),
       (15, 4),
       (16, 5),
       (17, 6),
       (18, 6),
       (19, 6),
       (20, 7),
       (21, 7),
       (22, 7),
       (23, 7),
       (24, 7),
       (25, 8),
       (26, 8),
       (27, 8),
       (28, 9),
       (29, 9),
       (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 1),
       (20, 1),
       (21, 1),
       (22, 1),
       (23, 1),
       (24, 1),
       (25, 1),
       (26, 1),
       (27, 1),
       (28, 2),
       (29, 2);

INSERT into ALLERGIE(id, name)
values (nextval('AL_SEQ'), 'lactose'),
       (nextval('AL_SEQ'), 'noten'),
       (nextval('AL_SEQ'), 'gluten'),
       (nextval('AL_SEQ'), 'soya'),
       (nextval('AL_SEQ'), 'pinda'),
       (nextval('AL_SEQ'), 'eieren');

insert into PRODUCT_ALLERGIES(PRODUCTS_ID, ALLERGIES_ID)

values (1, 1),
       (2, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (10, 2),
       (19, 1),
       (20, 1),
       (21, 1),
       (22, 1),
       (56, 1),
       (56, 6),
       (56, 3),
       (57, 1),
       (57, 6),
       (57, 3),
       (58, 1),
       (58, 6),
       (58, 3),
       (59, 1),
       (59, 6),
       (59, 3),
       (66, 3),
       (66, 1),
       (67, 3),
       (67, 1),
       (69, 6),
       (69, 1),
       (70, 6),
       (70, 1);

insert into product_category(id, name)
values (nextval('CAT_SEQ'), 'alcoholisch'),
       (nextval('CAT_SEQ'), 'vegetarisch');

insert into product_categories(products_id, categories_id)
values (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (28, 1),
       (29, 1),
       (30, 1),
       (31, 1),
       (32, 1),
       (33, 1),
       (34, 1),
       (35, 1),
       (36, 1),
       (37, 1),
       (38, 1),
       (39, 1),
       (40, 1),
       (41, 1),
       (42, 1),
       (43, 1),
       (44, 1),
       (45, 1),
       (46, 1),
       (47, 1),
       (48, 1),
       (49, 1),
       (50, 1),
       (51, 1),
       (52, 1),
       (53, 1),
       (54, 1),
       (55, 1),
       (60, 1),
       (69, 2);

insert into CONTACT_TYPE(id, question_type, domain_id)
values (1, 'Algemene vraag', 1),
       (2, 'Reservering', 1),
       (3, 'Iets anders', 1);

insert into contact_form(id, name, onderwerp, email, contact_type_id, question, timestamp, read)
values (nextval('CF_SEQ'), 'persona A', 'onderwerp1', 'email1@example.com', 1,
        'this is a placeholder text merely to show how it works', '2021-03-30 18:34:55.698', false),
       (nextval('CF_SEQ'), 'persona B', 'onderwerp2', 'email2@example.com', 1,
        'this is a placeholder text merely to show how it works', '2021-02-12 12:37:55.698', false),
       (nextval('CF_SEQ'), 'persona C', 'onderwerp3', 'email3@example.com', 2,
        'this is a placeholder text merely to show how it works', '2020-12-03 14:56:55.698', false),
       (nextval('CF_SEQ'), 'persona D', 'onderwerp4', 'email4@example.com', 3,
        'this is a placeholder text merely to show how it works', '2021-03-28 19:12:55.698', false),
       (nextval('CF_SEQ'), 'persona E', 'onderwerp5', 'email5@example.com', 2,
        'this is a placeholder text merely to show how it works', '2021-03-25 12:35:55.698', false),
       (nextval('CF_SEQ'), 'persona F', 'onderwerp6', 'email6@example.com', 1,
        'this is a placeholder text merely to show how it works', '2021-03-23 10:45:55.698', false),
       (nextval('CF_SEQ'), 'persona G', 'onderwerp7', 'email7@example.com', 2,
        'this is a placeholder text merely to show how it works', '2021-03-27 16:24:55.698', false);