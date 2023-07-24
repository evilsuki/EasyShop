# FinalCapstone_EasyShop_Team_1

***EasyShop***

**DESCRIPTION**

EasyShop is an online store application that allows users to browse the website, make an account, log in/out of their account, and add available products to their cart.

When users first visit the site, they are met with the home screen that shows the avaiblable products. The user is given the option to make an account by creating a username and password. The customers information is then encrypted for security and saved to the database. 

Once a customer logs into their account, they are able to create a profile with their personal infomation. While logged in, users are able to add desired products to their carts. If a customer adds more than one of the same product, the quantity for that product is increased. The user also has the option to remove items, or clear their cart. The total amount of all items is calculated and displayed within the cart. If the user deicded to log out of their account, their cart is saved, and will still hold their items when the user logs back in. 

The online store runs off of a database. The database stores all information for the store, including users' account information, store inventory, users' carts, product information, and user profile information. The information in the database can be added, updated, and deleted as users navigate the website.

**INTERESTING CODE**

The ShoppingCart class is interesting because the getTotal method uses Java's stream operations to calculate the total value of the items in the cart, and returns a BigDecimal representing the total value.

![Screenshot (550)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/d8300a08-bc32-4092-aac6-7585f6b1a337)


**VISUALS**

![Screenshot (546)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/0b9e0e0a-ba1f-4191-bbd1-4fb8681d492f)
![Screenshot (547)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/1b641524-be81-4c33-affe-fc609a9ca107)
![Screenshot (549)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/86c9cefe-dde9-4a6b-abe6-bf4d68effc06)
![Screenshot (548)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/e0a6e070-c194-4285-9c20-1afd75aac54a)
![Screenshot (551)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/c30508cd-75c4-45cb-abec-aeda0690d9d7)
![Screenshot (552)](https://github.com/gdzierzon/FinalCapstone_EasyShop_Team_1/assets/130028689/d3a5ff99-650b-4dac-a239-9e0e01604e74)
