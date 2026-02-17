# Projet E-commerce Microservices

Ce projet est une plateforme e-commerce moderne construite avec **Spring Boot** et **Spring Cloud**. Il illustre une architecture microservices avec découverte de services, configuration centralisée, passerelle API, communication asynchrone via Kafka, et persistance polyglotte (PostgreSQL et MongoDB).

## Architecture

Le système est composé des microservices suivants :

- **Config Server** – configuration centralisée pour tous les services.
- **Discovery Service (Eureka)** – enregistrement et découverte des services.
- **Gateway** – point d'entrée unique pour les clients, routage des requêtes.
- **Product Service** – gestion du catalogue de produits et catégories (PostgreSQL).
- **Customer Service** – gestion des clients (MongoDB).
- **Order Service** – gestion des commandes et lignes de commande (PostgreSQL). Communique avec Product Service et Payment Service via Feign.
- **Payment Service** – traitement des paiements (PostgreSQL). Envoie des événements Kafka.
- **Notification Service** – écoute les événements Kafka et envoie des notifications par email (MongoDB).

## Technologies

- Java 17
- Spring Boot 3.2.4
- Spring Cloud 2023.0.1
- Spring Data JPA / Hibernate
- Spring Data MongoDB
- Spring Kafka
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Feign
- Lombok
- PostgreSQL
- MongoDB
- Apache Kafka
- Docker / Docker Compose
- Maven

## Services et Endpoints

Tous les services tournent en local. Les ports par défaut sont indiqués ci-dessous. La Gateway (port 8222) expose les mêmes endpoints sous `/api/v1/...` et route vers le service approprié.

### 1. Config Server (port `8888`)
- Fournit la configuration à tous les services.
- Pas d'endpoint métier.

### 2. Discovery Service (Eureka) (port `8761`)
- Interface web : `http://localhost:8761`
- Pas d'endpoint métier.

### 3. Gateway (port `8222`)
- Route les requêtes vers les services en fonction du chemin.
- Exemple : `http://localhost:8222/api/v1/products` → Product Service.

### 4. Product Service (port `8050`)
URL de base : `http://localhost:8050/api/v1`

| Méthode | Endpoint          | Description                   
|--------|-------------------|---------------------------------|
| POST   | `/categories`     | Créer une nouvelle catégorie    | 
| POST   | `/products`       | Créer un nouveau produit        | 
| POST   | `/products/purchase` | Acheter plusieurs produits (lot) |
| GET    | `/products/{id}`  | Récupérer un produit par son ID |  
| GET    | `/products`       | Lister tous les produits        | 

### 5. Customer Service (port `8090`)
URL de base : `http://localhost:8090/api/v1/customers`

| Méthode | Endpoint       | Description                  
|--------|----------------|------------------------------|
| POST   | `/`            | Créer un nouveau client      | 
| PUT    | `/`            | Mettre à jour un client      |
| GET    | `/`            | Lister tous les clients      | 
| GET    | `/exits/{id}`  | Vérifier si un client existe | 
| GET    | `/{id}`        | Récupérer un client par ID   | 
| DELETE | `/{id}`        | Supprimer un client          | 

### 6. Order Service (port `8070`)
URL de base : `http://localhost:8070/api/v1`

| Méthode | Endpoint         | Description                    |
|--------|------------------|---------------------------------|
| POST   | `/orders`        | Créer une nouvelle commande     | 
| GET    | `/orders`        | Lister toutes les commandes     | 
| GET    | `/orders/{id}`   | Récupérer une commande par ID   | 
| GET    | `/order-lines/order/{orderId}` | Récupérer les lignes d'une commande | 

### 7. Payment Service (port `8060`)
URL de base : `http://localhost:8060/api/v1/payments`

| Méthode | Endpoint | Description          | 
|--------|----------|----------------------|
| POST   | `/`      | Créer un nouveau paiement | 

### 8. Notification Service (port `8040`)
- Pas d'endpoint REST. Il écoute les topics Kafka et envoie des emails.

## Prérequis

- JDK 17
- Maven 3.8+
- Docker et Docker Compose (pour les bases de données et Kafka)

## Installation et exécution

### 1. Démarrer l'infrastructure (PostgreSQL, MongoDB, Kafka, etc.)

```bash
cd services
docker-compose up -d
```

Cela lance :
- PostgreSQL sur `localhost:5432` (bases : `product`, `order`, `payment`)
- MongoDB sur `localhost:27017`
- Kafka sur `localhost:9092`
- Zookeeper
- MailDev (faux serveur SMTP) sur `localhost:1025` (SMTP) et `localhost:1080` (interface web)
- pgAdmin sur `localhost:5050`
- Mongo Express sur `localhost:8081`

```

Tous les services s'enregistrent dans Eureka. Vérifiez sur `http://localhost:8761`.

## Configuration

- Chaque service possède son propre `application.yml` (ou lit depuis Config Server). Les profils par défaut utilisent `localhost` pour les bases de données et Kafka.
- Pour désactiver Kafka (tests rapides sans broker), mettez `spring.kafka.bootstrap-servers: ""` dans la configuration de chaque service.
- Les emails sont envoyés vers un serveur SMTP factice (MailDev). Visualisez-les sur `http://localhost:1080`.

## Tester avec Postman

Importez la collection Postman fournie (fichier `ecommerce-microservices.postman_collection.json`) qui contient toutes les requêtes. La collection utilise des variables pour les URLs de base et stocke automatiquement les IDs.

**Flux typique :**

1. Créer une catégorie → stocker `categoryId`
2. Créer deux produits → stocker `productId1`, `productId2`
3. Créer un client → stocker `customerId`, `firstName`, `lastName`, `email`
4. Créer une commande avec les IDs ci-dessus → stocker `orderId`
5. Créer un paiement avec `orderId` et les données du client

## Topics Kafka

- `order-topic` – émis par Order Service après création d'une commande.
- `payment-topic` – émis par Payment Service après traitement d'un paiement.
- Notification Service consomme ces deux topics.

