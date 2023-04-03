# Announcement ПРИМЕЧАНИЕ



    @PostMapping("/create") - метод создания объявления, можно использовать примерные значения. id устанавливается по дефолту, 
    для проверки сортировки поставить разные цены в прайс 
    {
    "title": "Example Title",
    "photos": [
        "https://example.com/photo1.jpg",
        "https://example.com/photo2.jpg",
        "https://example.com/photo3.jpg"
    ],
    "description": "Example description.",
    "price": 99.99
     }
     
    @GetMapping("/list10") - метод который вытаскивает первые n объявление, число n можно изменить в сервисе, 27 строка полe size(по дефолту стоит 6)
    для проверки сортировки использовать http://localhost:2020/announcements/list10?sortBy=price&sortOrder=DESC
    
    sortBy - что сортировать(price или createdAt) по дефолту стоит создание
    sortOrder - как сортировать(asc - возрастание, desc - убывание)
    
    @GetMapping("/{id}") - метод по иска по айди, вытаскивает название объявления, цена, ссылку на главное фото(первое фото) и desc - null
    
    опциональные поля. можно вытащить все ссылки на фото, добавив ?allPhotos=true или полное описание allDescription=true
    все вместе http://localhost:2020/announcements/2?allPhotos=true&allDescription=true
    по дефолту значения все false
 
