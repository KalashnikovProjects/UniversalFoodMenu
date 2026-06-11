# <img src="images/UFM.png" width="35"> Universal food menu
### Mobile app to display the food menu on the TV 🍔
<details><summary>Screenshots</summary>
<p>Mobile app</p>
<img src="images/main_menu.jpg" width="200" alt="Mobile main menu"/>
<p>Code for connect TV screen</p>
<img src="images/code_on_tv.jpg" width="400" alt="TV screen display"/>
<p>TV and design editor</p>
<img src="images/design_editor.jpg" width=500 alt="Design editor"/>
</details>

## <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/1280px-YouTube_full-color_icon_%282017%29.svg.png" height="15" alt="Youtube"/> Demo video

http://www.youtube.com/watch?v=qaltowj_-7A

[![DEMO VIDEO](http://img.youtube.com/vi/qaltowj_-7A/0.jpg)](http://www.youtube.com/watch?v=qaltowj_-7A "Demo video")

## Architecture

### Backend
* <img src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/kotlin.png" height="12" alt="Kotlin"/> Kotlin
* <img src="images/ktor_logo.png" width="15" alt="Ktor"/> Ktor - REST API server, use websocket for sending events of changing data. For authorization using JWT tokens.
* Database - <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Postgresql_elephant.svg/500px-Postgresql_elephant.svg.png" height="12" alt="Postgres"/> PostgreSQL
* DI - <img src="https://insert-koin.io/img/koin_new_logo.png" height="15" alt="Koin"/> Koin
* <img src="images/Docker_Logo.png" height="12" alt="Docker"/> Docker

### Frontend (Mobile app & Android TV app)
* Clean arch, MVVM
* <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Jetpack_Compose_logo.png/500px-Jetpack_Compose_logo.png" height="15" alt="Jetpack Compose"/> Jetpack Compose
* <img src="images/ktor_logo.png" width="15" alt="Ktor"/> Ktor client
* DI - Dagger Hilt 2

## Data-scheme

### Database scheme
```mermaid
erDiagram
    USERS {
        uint id PK
        text username UK
        text password_hash
    }

    CATEGORIES {
        uint id PK
        text name
        text image_uri "nullable"
        float price "nullable"
        bool in_stock "nullable"
        uint user_id FK
    }

    FOOD_ITEMS {
        uint id PK
        text name
        float price
        text image_uri "nullable"
        bool in_stock
        uint user_id FK
    }

    FOOD_ITEMS_CATEGORIES {
        uint category_id PK, FK
        uint food_item_id PK, FK
    }

    TEXT_ITEMS {
        uint id PK
        text text
        uint user_id FK
    }

    IMAGE_ITEMS {
        uint id PK
        text image_uri
        uint user_id FK
    }

    SCREENS {
        uint id PK
        text name
        int width
        int height
        text style
        uint user_id FK
    }

    DESIGN_ITEMS {
        uint id PK
        uint food_item_id FK "nullable"
        uint category_id FK "nullable"
        uint text_item_id FK "nullable"
        uint image_item_id FK "nullable"
        text style
        uint screen_id FK
        uint user_id FK
    }

    %% Relationships
    USERS ||--o{ CATEGORIES : "owns"
    USERS ||--o{ FOOD_ITEMS : "owns"
    USERS ||--o{ TEXT_ITEMS : "owns"
    USERS ||--o{ IMAGE_ITEMS : "owns"
    USERS ||--o{ SCREENS : "owns"
    USERS ||--o{ DESIGN_ITEMS : "owns"

    CATEGORIES ||--o{ FOOD_ITEMS_CATEGORIES : "has"
    FOOD_ITEMS ||--o{ FOOD_ITEMS_CATEGORIES : "belongs to"

    SCREENS ||--o{ DESIGN_ITEMS : "contains"
    FOOD_ITEMS |o--o{ DESIGN_ITEMS : "referenced in"
    CATEGORIES |o--o{ DESIGN_ITEMS : "referenced in"
    TEXT_ITEMS |o--o{ DESIGN_ITEMS : "referenced in"
    IMAGE_ITEMS |o--o{ DESIGN_ITEMS : "referenced in"
```

### Style JSON
The styles are inspired by CSS and are stored in JSON format like that:

```json
{
  "x": 100.5,
  "y": 250.0,
  "scale": 1.0,
  "notInStockStyle": "OPACITY",
  "textColorHex": "#FF5733",
  "showImage": true,
  "showPrice": false,
  "foodItemDisplayTypeStyle": {
    "type": "row"
  },
  "imageScale": 1.2,
  "itemWidthScale": 0.9,
  "categoryItemStyle": {"notInStockStyle":"CROSSED_OUT","textColorHex":"#000000","showImage":false,"showPrice":true,"foodItemDisplayTypeStyle":{"type":"cell"}}
}
```

## Launch

Backend server <img src="images/Docker_Logo.png" height="12" alt="Docker"/> docker image: https://hub.docker.com/r/kalashnik/ufm-server

APKs added to release
