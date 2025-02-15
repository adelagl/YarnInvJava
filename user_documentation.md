
# User Documentation - Yarn Inventory Management

  

## Introduction

This program allows users to manage their yarn inventory, plan projects, and estimate the required amount of yarn for knitting and crocheting projects. Users can:

-  **Add, remove, filter and merge duplicate yarns**.

-  **Estimate the amount of yarn required for a project**.

-  **Create, view and delete projects**.
---
## Installation and Running the Program

### 1. Requirements

- Java 17 or newer

- Apache Maven

### 2. Running the Program

If using Maven:

```sh

mvn  compile  exec:java

```

If running manually:

```sh

javac  -d  out  src/main/java/app/App.java

java  -cp  out  app.App

```

---

  

## Using the Program

After starting the program, the main menu appears. The user is asked to choose an option.
```

--- YARN INVENTORY ---

1. Add yarn

2. Remove yarn

3. Change the length of yarn

4. Merge yarns

5. Print inventory

6. Filter through inventory

7. Estimate yarn for a project

8. Plan a project

9. Manage projects

10. Exit

Enter your choice:

```
---

## Features

### 1. Adding Yarn

The user provides the brand, color, length, and weight of the yarn.

**Example input:**

```

Enter brand name: Alaska

Enter colour: Blue

Enter the length (in meters): 150

Enter the yarn weight (name or alias): Lace

```
---

### 2. Removing Yarn

The inventory is printed to the user. The selects the index of the yarn to remove.

**Example:**

```

1: Alaska - Blue - 150m (Medium)

2: WoolKing - Red - 200m (Bulky)

  

Enter the index of the item you want to remove: 1

```

The yarn at index `1` will be removed.

  

---

  

### 3. Changing Yarn Length

The user selects a yarn and specifies how many meters to decrease.

  

**Example:**

```

Enter the index of the item whose length you want to change: 2

Enter the amount you want to reduce: 50

```

The yarn will be shortened by 50 meters. If the length reaches 0 meters, the yarn is removed.


---

  

### 4. Merging Duplicate Yarns

If duplicate yarns exist in the inventory (same brand, color, and weight), they can be merged. The length of the yarn is not taken into account when comparing yarns.

**Before merging:**

```

1: YarnCo - Blue - 100m (Medium)

2: YarnCo - Blue - 150m (Medium)

```

After merging:

```

1: YarnCo - Blue - 250m (Medium)

```

---

### 5. Displaying Inventory

Lists all stored yarns. 

**Example output:**

```

1: YarnCo - Blue - 150m (Medium)

2: WoolKing - Red - 200m (Bulky)

```
---
### 6. Filtering Inventory

The user can filter yarn by:

-  **Color**

-  **Yarn weight**

-  **Brand**

**Example selection:**

```

1. By color

2. By yarn weight

3. By brand

  

Enter choice: 1

Enter color: Blue

```

The output will show all blue yarns.

---

### 7. Estimating Yarn for a Project

The user selects the type of project and yarn weight.
 

**Example:**

```

Here is a list of available projects:

1: Hat

2: Sweater

3: Scarf

Which project would you like to make? 2

Which yarn weight do you want to use? Medium

  

Min: 1125 meters

Max: 1625 meters

```
The app returns the minimum and maximum estimated length of yarn needed for the particular project.

---

### 8. Planning a Project

The user selects a project and adds yarns from their inventory.

**Example:**

```

You need 1125 meters of Medium weight yarn.

  

Your inventory (only Medium weight yarns are shown):

1: YarnCo - Blue - 150m (Medium)

2: WoolKing - Green - 500m (Medium)

  

Choose a yarn to add by entering its number: 2

Remaining length needed: 625 meters.

```

This process repeats until the user has selected enough yarn (or until the user runs out of yarn, in which case the process is terminated).

Once the required yarn is selected, the user can save the project to a file.

---


### 9. Managing Projects

Users can view all saved projects, see project details, or delete a project.

 
#### Viewing All Projects

```

Here is a list of all your projects:

1: MySweater

2: BabyBlanket

3: WinterHat

---------------------

What would you like to do?

1. See project detail

2. Delete project

3. Return to menu

```

  

#### Viewing Project Details

```

Enter project index: 2

Displaying details for project: BabyBlanket

Required yarn:

- WoolKing - White - 500m (Bulky)

- YarnCo - Grey - 700m (Medium)

```

  

#### Deleting a Project

```

Enter project index: 3

Project WinterHat deleted.

```

  

---

  

### 10. Exiting the Program

Selecting option 10 exits the program and saves the inventory automatically.

  

---

  

## Saving and Loading Inventory

- Yarn is **automatically saved** in `inventory.json` when the program closes.

- The inventory is **automatically loaded** on the next startup.
