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
(1, 'bistro'),
(2, 'bolo');

insert into page(
id, page_name, domain_id
)
values
(1, 'Home', 1),
(2, 'References', 1),
(3, 'Contact', 1),
(4, 'personeel', 1),
(5, 'menu', 1);

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
(2, 'https://www.pexels.com/', 'Pexels', 'Negative Space', 'https://www.pexels.com/@negativespace', 'header image'),
(3, null, null, 'Dries Delanghe', 'https://www.driesdelanghe.be/', 'site');

insert into contact_info(
id, info_name, info_content, domain_id, image_id
)
values
(1, 'email', 'info@lientjes-coffeebreak.be', 1, 8),
(2, 'adres', 'Stationstraat 143, 2845 Niel', 1, 7),
(3, 'telefoon', '03 344 85 79', 1, 9);

insert into menu_section(
id, domain_id, name
)
values
(1, 1, 'Warme Dranken'),
(2, 1, 'Koude Dranken'),
(3, 1, 'Ontbijt'),
(4, 1, 'Versnaperingen'),
(5, 1, 'Gerechten'),
(6, 1, 'Desserten'),
(7, 2, 'Pasta''s'),
(8, 1, 'Alle producten'),
(9, 2, 'Alle producten');

insert into menu_sub_section(
id, name
)
values
(1, 'Koffie'),
(2, 'Speciale Koffieskes'),
(3, 'Koffie met Alcohol'),
(4, 'Pure leaf'),
(5, 'Zakskes Thee'),
(6, 'Warme Melk'),
(7, 'Warme Choco'),
(8, 'Fris'),
(9, 'Streekbier'),
(10, 'Bieren op Vat'),
(11, 'Bieren op Fles'),
(12, 'Wijnen'),
(13, 'Cava'),
(14, 'Aperitief'),
(15, 'Alcohol'),
(16, 'Ontbijten tot 11u30'),
(17, 'Borrelhapjes' ),
(18, 'Dagmenu tot 17u00'),
(19, 'Kleine honger tot 17u00'),
(20, 'Starters'),
(21, 'Hoofdgerechten'),
(22, 'Salades'),
(23, 'Pasta'),
(24, 'Kindermenu'),
(25, 'Nagerechten'),
(26, 'Pannenkoeken & Wafels'),
(27, 'Bevrozen genot'),
(28, 'pastas'),
(29, 'Kindermenu');

insert into product(
id, menu_sub_section_id, name, price_in_eur, extra_info
)
values
(1, 1,  'Lungo', 2.10, null),
(2, 1,  'Americano', 2.30, null),
(3, 1,  'Potje koffie', 4.00, null),
(4, 1,  'Espresso', 2.10 , null),
(5, 1,  'Dubbele espresso', 2.60, null),
(6, 2,  'Chai Latte', 3.30, null),
(7, 2,  'White Mocha', 3.90, null),
(8, 2,  'Latte Vanille', 3.60, null),
(9, 2,  'Latte Hazelnoot', 3.60, null),
(10, 2, 'Latte Speculoos', 3.60, null),
(11, 3, 'Yellow Coffee', 4.90, null),
(12, 3, 'Irish Coffee', 7.10, null),
(13, 3, 'Italian Coffee', 7.10, null),
(14, 3, 'French Coffee', 7.10, null),
(15, 3, 'Hasseltse Koffie', 7.10, null),
(16, 4, 'Thee infuus', 2.80, null),
(17, 4, 'Tea for two', 5.00, null),
(18, 5, 'Rosenbottelthee', 2.10, null),
(19, 6, 'Warme verse Melk', 2.50, null),
(20, 6, 'Met extra smaakje', 0.90, 'Smaakjes vriij te kiezen: <br> hazelnoot - vanille - caramel - speculaas - kaneel'),
(21, 7, 'Warme verse Melk met echte Chocolade Callets van Callebaut',3.50, 'Keuze uit pire - melk of witte korrels'),
(22, 7, 'Met extra smaakje', 0.90, 'Smaakjes vriij te kiezen: <br> hazelnoot - vanille - caramel - speculaas - kaneel'),
(23, 8, 'Bru plat', 2.20, null),
(24, 8, 'Bru plat 0,5l', 4.10, null),
(25, 8, 'Bru bruis', 2.20, null),
(26, 8, 'Bru bruis 0,5l', 4.10, null),
(27, 8, 'Coca Cola Regular', 2.20, null),
(28, 9, 'Hellegat blond', 3.00, null),
(29, 9, 'Hellegat bruin', 3.00, null),
(30, 9, 'Rupeliaan', 3.80, null),
(31, 10, 'Stella 25cl', 2.30, null),
(32, 10, 'Stella 33cl', 2.90, null),
(33, 10, 'Leffe bruin', 3.60, null),
(34, 10, 'La Chouffe', 3.60, null),
(35, 10, 'Tripel d''Anvers', 3.60, null),
(36, 11, 'Jupiler', 2.50, null),
(37, 11, 'Vedett', 2.80, null),
(38, 11, 'Hoegaarden', 2.50, null),
(39, 11, 'Hoegaarden Rosé', 2.50, null),
(40, 11, 'Kriek Liefmans', 2.50, null),
(41, 12, 'Huiswijn wit of rood', null, null),
(42, 12, 'Glas', 3.50, null),
(43, 12, 'Karaf 0,5l', 10.00, null),
(44, 12, 'wijn 0,75l', 15.00, null),
(45, 13, 'Glas cava', 4.50, null),
(46, 13, 'Cava 0,75l', 18.00, null),
(47, 14, 'Lientje''s aperitief', 4.00, null),
(48, 14, 'Porto rood of wit', 4.00, null),
(49, 14, 'Sherry Pedro Domecq', 4.00, null),
(50, 14, 'Martini Blanco of Rosso', 4.00, null),
(51, 15, 'Bacardi', 6.00, null),
(52, 15, 'Jack Daniels', 6.00, null),
(53, 15, 'Amaretto', 4.00, null),
(54, 15, 'Bombay Gin', 6.00, null),
(55, 15, 'Eristoff Vodka', 6.00, null),
(56, 16, 'Vanaf zes personen wordt er roerei bij het ontbijt geserveerd', null, null),
(57, 16, 'Klassiek ontbijt', 11.00, 'Vers fruitsap, 1 koffie of thee, assortiment van broodjes, mini croissant, mini koffiekoekjes, yoghurt , kaas, ham, confituur, choco, speculaaspasta, 1 eitje naar keuze'),
(58, 16, 'Luxe-ontbijt', 5.00, 'Vers fruitsap, 1 koffie of thee, assortiment van broodjes, yoghurt , mini croissant, mini koffiekoekjes, uitgebreider assortiment van zoet en hartig beleg met Franse kaas, 1 eitje naar keuze'),
(59, 16, ' Kinderontbijtje', 7.00, 'Verse fruitsap of fristi of chocolademelk, chocoladebroodje, sandwich, confituur, choco & speculaaspasta, verrassing'),
(60, 16, 'Cava', 21.50, 'Vers fruitsap, glas cava met Scottish toast (gerookte zalm en roereitje) assortiment van zoet en hartig beleg, Franse kaas, 1 koffie of thee, assortiment van broodjes, croissant, koffiekoekjes.'),
(61, 17, 'Portie kaas of salami', 5.50, null),
(62, 17, 'Portie gemend', 8.00, null),
(63, 17, 'Portie warme hapjes', 12.00, null),
(64, 17, 'Tapasplanke', 15.00, null),
(65, 17, 'Breekbrood', 11.00, null),
(66, 18, 'Dagelijkse aanbieding met soep en hoofdgerecht', 14.95, null),
(67, 19, 'Boterham met ham of kaas', 8.00, null),
(68, 19, 'Boterham jam en kaas', 10.00, null),
(69, 19, 'Veggie sandwich', 8.00, null),
(70, 19, 'Omelet of spiegelei', 8.00, null),
(71, 19, 'Omelet champignon', 9.50, null),
(72, 20, 'Hartige dagsoep', 5.50, null),
(73, 20, 'Rundscarpaccio', 12.00, null),
(74, 20, 'Kaas Kroketten 2st. met garni.', 12.00, null),
(75, 20, 'Garnaal kroketten 2st. met garni.', 15.50, null),
(76, 20, 'Duo van kaas en garnaal kroketten', 13.50, null),
(77, 21, 'Vidé huisgemaakt', 15.00, null),
(78, 21, 'Soep van Trien den heilige', 15.00, null),
(79, 21, 'Witloof in den oven', 15.00, null),
(80, 21, 'Steak met poivre vert of archiduc saus', 19.00, 'Geserveerd met koude garnituur'),
(81, 21, 'Kipfilet met poivre vert of archiduc saus', 16.00, 'Geserveerd met koude garnituur'),
(82, 22, 'Salade niçoise met tonijn', 17.00, null),
(83, 22, 'Scampi salade (6st)', 18.00, null),
(84, 22, 'Sla met geitenkaas, spekjes en honingdressing', 17.00, null),
(85, 23, 'Bololientjes', 14.00, null),
(86, 23, 'Carbonara', 14.00, null),
(87, 23, 'Veggie', 14.00, null),
(88, 23, 'Scampi (6st) met pikante roomsaus', 19.00, null),
(89, 24, 'Kids Vidé', 8.50, null),
(90, 24, 'Kids schep van heilige Trien', 8.50, null),
(91, 24, 'Kids Bolo', 8.50, null),
(92, 24, 'Croque uit het vuistje', 7.00, null),
(93, 24, 'Kids friet met mayo of ketchup', 4.50, null),
(94, 25, 'Gebak van de dag', 5.00, null),
(95, 25, 'Gebak van de dag + koffie', 6.50, null),
(96, 25, 'Appelstrüdel met ijs en slagroom', 9.00, null),
(97, 25, 'Panacota', 10.00, null),
(98, 25, 'Moelleux au chocolat', 12.00, null),
(99, 26, 'Pannekoek', 5.50, null),
(100, 26, 'Pan banan', 12.00, null),
(101, 26, 'Pannenkoek met vanilla ijs en warme choco', 8.50, null),
(102, 26, 'Normandische pannenkoek', 14.50, null),
(103, 26, 'Wafel', 5.50, null),
(104, 27, 'Coupe vanille', 7.50, null),
(105, 27, 'Bananasplit', 9.00, null),
(106, 27, 'Dame Blanche', 8.00, null),
(107, 27, 'Coupe aardbei (enkel seizoen)', 10.00, null),
(108, 27, 'Coupe vers fruit', 10.00, null),
(109, 28, 'Bololientjes', 14.00, null),
(110, 29, 'Kids Bolo', 8.50, null);

insert into menu_section_menu_sub_section_list(
    menu_sub_section_list_id, menu_section_list_id
)
values
(1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,2),(9,2),(10,2),(11,2),(12,2),(13,2),(14,2),(15,2),
(16,3),(17,4),(18,4),(19,4),(20,5),(21,5),(22,5),(23,5),(24,5),(25,6),(26,6),(27,6),(28,7),(29,7),
(1,8),(2,8),(3,8),(4,8),(5,8),(6,8),(7,8),(8,8),(9,8),(10,8),(11,8),(12,8),(13,8),(14,8),(15,8),
(16,8),(17,8),(18,8),(19,8),(20,8),(21,8),(22,8),(23,8),(24,8),(25,8),(26,8),(27,8),(28,9),(29,9)