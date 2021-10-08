# FoodTracker (Team Project)
FoodTracker is a food waste management app that helps users manage their groceries and reduce
food waste. The app holds an inventory of the current groceries that the user can view to tell
which food items will expire soon. To add an item to the inventory, the app uses a machine
learning model that recognizes food items in a photo taken by the user. The user can then verify
the correct food item was identified and enter the corresponding expiration date. With these
inputs, the app adds the item name and its expiration date in the items list which is stored in
Google Cloud’s Firestore Database. The user can view and interact with their inventory of food
with their expiration dates with the app’s real time listener to the cloud-hosted database.
Moreover, FoodTracker includes a manual entry option in case of any inaccuracy in recognizing
the item. This app uses Inceptionv3 pre-trained model for food items category classification.

Used Technologies and languages: Figma, Java, XML, Python, and jupyter notebook.
