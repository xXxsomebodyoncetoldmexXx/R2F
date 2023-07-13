# R2F
Burp Suite extension to convert current request to multipart/form-data


## How to use
- Download file jar and add to Burp Suite.
- In repeater tab, right-click and select `convert`

## Notes:
If the data is a file, remember to structure it like this:
```http
Content-Disposition: form-data; name="foo"; filename="bar"
Content-Type: text/plain; charset=utf-8
```

## Todos:
- Improve convert flow: select the parameter/key that is the name of the file > convert that file with proper `filename` field and `Content-Type` > the rest is the same.
- Make a shortcut for faster converting.
- Write better README.md