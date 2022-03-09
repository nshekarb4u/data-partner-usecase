Use case:
=========
We are looking at the information from our data partner and find out the interest of people based on their submissions.
The data structure is in JSON and looks as below:

[
    {
    "id" : 1,
    "Name" : "Jane Doe",
    "Age" : 30,
    "Address" : "NYC, USA",
    "ZIP" : "10001",
    "Interest" : "Sports, Movies, Reading Books"
    }, 
    {
    "id" : 2,
    "Name" : "John Doe",
    "Age" : 32,
    "Address" : "NYC, USA",
    "ZIP" : "10001",
    "Interest" : "Racing, Reading Books, Trekking"
    }
]

Program Execution:
==================
*. Run the main class `CustomerInterestsApp.scala`, which expects a single program argument.
    1 ==> to create csv output for category of interests for all the users.
    2 ==> aggregate `interest categories by zipCode`.
    Both ==> covers above-mentioned both the cases. 