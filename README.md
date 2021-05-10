# Expense Recorder

**About the App**:
Expense Recorder helps you check your monthly/yearly transactions and you can add new, update and delete each transaction.



**About the Project**:
This project is build on Android Studio 4.1.3 using min SDK as 21 in Kotlin Programming Language. 
Project follows MVVVM architecture and mai dependecies used in this project are of Room , Lifecycle and Kotlin Components.



**MileStone 1 Summary**
1. I have created a transaction table with fields - val id: Int, val amount: Double, val date: Long, val type: String.val comment: String. val transaction_type: String.
2. Created dashboard with expandable fab button for adding income and expense.
3. Created two new screens, 1 for Adding income and the other for addimg expense.
4. To add code resuablility, I did not create a separate screen for editing transaction, used the same ones we are using for adding transactions.
5. Added queries for insertion, updation, deletion and get current month data.



**Milestone 2**

 Asumption - 
   For filter search - Since there is no option for selecting year in the wireframes, I assumed the months would be of current year. 
   I have additionally added a year filter, if the user wishes to see the transactions for previous year. 

Summary - 
 1. Fetching data according to the selected year and month from the database. 
 2. Searching the  list for keywords and adding it to a different list of same object type, and later updating the recyclerview adapter with the filteed list.



**App Screenshots** -

<img align="left" width="200" height="400" alt="homess" src="https://user-images.githubusercontent.com/46419030/117587851-8a849c00-b13d-11eb-91ac-ef12aab51bc3.png">      <img align="left" width="200" height="400" alt="expense ss" src="https://user-images.githubusercontent.com/46419030/117587848-89536f00-b13d-11eb-94e0-fdeaa391124d.png"> 

  <img align="left" width="200" height="400" alt="edit_income" src="https://user-images.githubusercontent.com/46419030/117587854-8bb5c900-b13d-11eb-976d-25d03a2a6414.png">                    <img align="left" width="200" height="400" alt="view_transactions" src="https://user-images.githubusercontent.com/46419030/117587853-8b1d3280-b13d-11eb-97ce-9e8fe395c029.png">
  
  
  
  
  **APK Link** - https://i.diawi.com/ZzybWJ

