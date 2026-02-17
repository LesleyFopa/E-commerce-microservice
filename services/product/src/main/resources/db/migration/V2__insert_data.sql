-- Insertion de catégories
INSERT INTO public.category (id, name, description) VALUES
                                                 (nextval('category_seq'), 'Électronique', 'Ordinateurs, téléphones, accessoires'),
                                                 (nextval('category_seq'), 'Vêtements', 'Mode homme, femme et enfant'),
                                                 (nextval('category_seq'), 'Livres', 'Romans, essais, livres techniques'),
                                                 (nextval('category_seq'), 'Maison', 'Décoration, cuisine, mobilier'),
                                                 (nextval('category_seq'), 'Sports', 'Équipements et vêtements de sport');

-- Insertion de produits (chaque produit est lié à une catégorie via category_id)
-- On récupère les ID des catégories précédentes en supposant l'ordre d'insertion.
-- Si vous avez inséré dans l'ordre ci-dessus, les ID seront :
-- Électronique : 1, Vêtements : 51, Livres : 101, Maison : 151, Sports : 201
-- (car les séquences commencent à 1 et incrémentent de 50)
-- Vous pouvez vérifier avec SELECT * FROM category; pour connaître les ID exacts.

-- Produits pour la catégorie Électronique (id = 1)
INSERT INTO public.product (id, name, description, available_quantity, price, category_id) VALUES
                                                                                        (nextval('product_seq'), 'Smartphone XYZ', 'Écran 6.5", 128 Go, double SIM', 25, 299.99, 1),
                                                                                        (nextval('product_seq'), 'Ordinateur portable ABC', 'Intel i5, 8 Go RAM, 512 Go SSD', 10, 599.99, 1),
                                                                                        (nextval('product_seq'), 'Casque audio sans fil', 'Bluetooth, réduction de bruit', 50, 89.90, 1);

-- Produits pour la catégorie Vêtements (id = 51)
INSERT INTO public.product (id, name, description, available_quantity, price, category_id) VALUES
                                                                                        (nextval('product_seq'), 'T-shirt coton', 'Unisexe, couleur noir, taille M', 100, 12.50, 51),
                                                                                        (nextval('product_seq'), 'Jean slim', 'Bleu foncé, taille 32/32', 35, 39.99, 51),
                                                                                        (nextval('product_seq'), 'Veste imperméable', 'Légère, avec capuche', 15, 59.90, 51);

-- Produits pour la catégorie Livres (id = 101)
INSERT INTO public.product (id, name, description, available_quantity, price, category_id) VALUES
                                                                                        (nextval('product_seq'), 'Le Guide du voyageur', 'Récit de voyage', 8, 14.95, 101),
                                                                                        (nextval('product_seq'), 'Programmation Java', 'Apprentissage par la pratique', 20, 29.99, 101),
                                                                                        (nextval('product_seq'), 'Cuisine pour débutants', 'Recettes simples', 12, 9.90, 101);

-- Produits pour la catégorie Maison (id = 151)
INSERT INTO public.product (id, name, description, available_quantity, price, category_id) VALUES
                                                                                        (nextval('product_seq'), 'Lampe de chevet', 'Design moderne, LED', 30, 24.99, 151),
                                                                                        (nextval('product_seq'), 'Ensemble de couverts', '24 pièces, inox', 40, 34.50, 151),
                                                                                        (nextval('product_seq'), 'Coussin décoratif', '45x45 cm, gris', 60, 12.90, 151);

-- Produits pour la catégorie Sports (id = 201)
INSERT INTO public.product (id, name, description, available_quantity, price, category_id) VALUES
                                                                                        (nextval('product_seq'), 'Tapis de yoga', 'Anti-dérapant, 6 mm', 25, 19.99, 201),
                                                                                        (nextval('product_seq'), 'Haltères 5 kg', 'Paire, caoutchouté', 18, 29.90, 201),
                                                                                        (nextval('product_seq'), 'Ballon de football', 'Taille 5, cuir synthétique', 22, 15.50, 201);