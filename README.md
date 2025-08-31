
# **Mad Max: Road to Gas Town**
## **Project Concept**

### Primary Idea:
A post-apocalyptic journey simulation game set in the world of "Mad Max: Fury Road", where players make the decisions for the main character Max Rockatansky and drive a vehicle (War Rig) across a dangerous wasteland, visiting outposts, managing cargo, fuel, and avoiding ambushes. The game features strategic planning, resource management, and randomized events that affect the outcome of the journey.

### Key Classes:
- **Vehicle (WarRig)**: Represents the player's vehicle with attributes like fuel, cargo capacity, credits, and fuel consumption.
- **Outpost**: Represents outposts across the wasteland with fuel supply, items, and probability distribution of different events.
- **Item**: Represents cargo items with value and weight.
- **Step**: Represents a journey step between two outposts.
- **JourneyUtils**: Handles finding shortest routes, detours, journey simulation, random events, and Monte Carlo success rate calculations.

### Data Structures:
- **ArrayList**: Used for storing outposts, routes, and cargo items.
- **PriorityQueue**: Used in Dijkstra's algorithm
- **JSON**: Used for storing and retrieving outpost data, route information, and cargo items.

### Functionalities:
- Plan and simulate journeys between outposts.
- Refuel vehicle and manage fuel consumption at each outpost.
- Handle cargo using **0/1 Knapsack algorithm** for optimal profit.
- Dynamic response to random events such as ambushes and detours.
- Use **Dijkstra’s algorithm** to find shortest paths and **BFS** for detours.
- Display real-time journey progress and statistics.
- Save final journey stats (profit, fuel consumption, events, etc.) with completion timestamp to a file.
- Count-up animations and other GUI effects for immersive feedback.
- Full JavaFX GUI with custom fonts, colors, and animations for different windows.

### Additional Information:
- JavaFX is used for GUI with three distinct windows and custom color palettes.
- Custom fonts and CSS are applied for consistent style.
- Monte Carlo simulation generates random events at the outposts based on event probability distributions
- Monte Carlo simulation runs **1000 trials** to calculate success probabilities.
- JSON files store outpost and route data for persistent simulation.
- Edge cases (like fuel running out, ambushes, or cargo limits) are fully handled.
- Final stats window allows saving results to the **Downloads** folder.
- The game includes pop-up notifications, sliding/fade animations, and interactive GUI elements.
- Fun Factor: Random events keep the journey unpredictable, making each simulation unique.
