# ༼ つ ◕_◕ ༽つ Survival simulation 


<p align="center">
  <img src="https://img.shields.io/badge/Java-22-blue" alt="Java Version" />
  <img src="https://img.shields.io/badge/JavaFX-21-orange" alt="JavaFX Version" />
  <img src="https://img.shields.io/badge/Tests-JUnit%204-blue" alt="Tests" />
  <img src="https://img.shields.io/badge/Tests%20Passed-Passing-brightgreen" alt="Tests Passed" />
</p>

---

This Java project runs a simulation of a random population of peasants that eat carrots and try to survive.
The concept is inspired by this video from CodeBH YouTube channel : https://youtu.be/qVOjXQUzOJw?si=32QMRubXAz1onQ3U

---
## ✨Features

- ✅ `Main`
    - The `Main` file, when run, displays the UI of the simulation. You can launch it, pause it as you want. The simulation
        data will be automatically saved in the log files in `resources/logs`.

- ✅ `Logger`
    - The `Logger` file, when run, runs a simulation of 100 days dans stores the logs automatically. This class is meant 
        to give the possibility of generating quickly a large amount of data for analysis.

---
## (●ˇ∀ˇ●) Usage
Just clone the repo, add javaFX 21 to your project and run the `Main` or the `Logger` and that's it! ✪ ω ✪

---
## (☞ﾟヮﾟ)☞  Project Structure

    src
    └── main.java
        ├── Game.java                    # Core game simulation logic
        ├── components
        │    ├── Element.java            # Base part of each entity in the simulation
        │    ├── House.java              # House
        │    ├── Peasant.java            # Peasant
        │    └── eatable
        │         ├── Eatable.java       # Eatable abstract class
        │         └── Carrot.java        # Carrot 
        │
        ├── gui
        │    └── Main.java               # JavaFX application entry point
        │
        ├── logger
        │    └── Logger.java             # Log system implementation to save records, logs
        │
        └── utilities
            ├── Coordinates.java        # Definition of Coordinates
            ├── Movable.java            # Movable interface
            └── Preconditions.java      # Checking utility class
    
    
    resources
    ├── logs
    │    ├── data_exploitable            # Structured log data for analysis explotation
    │    └── verbal                      # Logs more humand-readable
    │
    └── style.css
        └── simulation.css               # JavaFX stylesheet
    
    
    test
    └── main.java.utilities
        └── MyCoordinatesTest.java       # Unit tests for Coordinates

---
## (╯°□°）╯︵ ┻━┻ Example logs 
**Verbal logs**

    ============================================================
                SIMULATION ENGINE — STARTUP
    ============================================================
    
    Simulation ID   : 2026-02-25T10:13:54.412498600
    Launch Time     : 2026-02-25T10:13:54.412498600
    Status          : INITIALIZING
    
    --- WORLD SNAPSHOT -----------------------------------------
    
    Houses          : 8
    Max peasant per house : 10
    (45,13);(29,20);(32,13);(22,17);(24,15);(46,30);(53,21);(15,24);
    Population      : 48
    Quantity to eat : 3
    
    Eatables        : 160
    Eatable ratio   : 20
    Map Size        : 60 x 40
    Period          : HELIOS
    
    --- SYSTEM INFO --------------------------------------------
    
    Thread          : JavaFX Application Thread
    Java Version    : 22.0.2
    ============================================================
    Day 0
    ============================================================
    
    Period          : HELIOS
    Status          : RUNNING
    
    --- POPULATION ---------------------------------------------
    
    Total Population: 48
    ============================================================
                            Day 1
    ============================================================
    Period          : HELIOS
    Status          : RUNNING
    --- POPULATION ---------------------------------------------
    Total Population: 66
    Population evolution: 37.5 %
    
    ============================================================
    Day 2
    ============================================================
    Period          : HELIOS
    Status          : RUNNING
    --- POPULATION ---------------------------------------------
    Total Population: 76
    Population evolution: 15.151515151515152 %
    
    ============================================================


**Data logs**

    # Simulation Metadata
    # simulation_id=2026-02-25T10:10:38.452758600
    # houses_count=15
    # max_peasant_per_house=10
    # eatables_ratio=20
    # houses_coordinates=(25,4);(55,33);(9,6);(18,1);(22,11);(54,16);(25,25);(0,31);(58,28);(34,21);(54,1);(8,2);(53,25);(47,15);(55,23);
    day,period,total_population,delta_population_percent
    0,HELIOS,82
    1,HELIOS,100,21.951219512195124
    2,HELIOS,112,12.0
    3,HELIOS,126,12.5
    4,HELIOS,142,12.698412698412698
    5,HELIOS,147,3.5211267605633805
    6,HELIOS,150,2.0408163265306123
    7,HELIOS,150,0.0

---
##  (┬┬﹏┬┬) Tests
We provided some basic tests in the test directory for `Coordinates` just to be sure the
instantiation was good as well as the `equals` method but for the main code the only test is to try it and verify by hand. 
O_O

---
## 🔄 You can now make it your own and update it !!! 
You can simply add new `Eatable` if you want or you can change all the parameters and static final constants in `Game` 
or in the other classes. Most of the constants are in `Game` but some aren't such as: `MAX_HEALTH` of a `Carrot` in
`Carrot` class; `DEFAULT_CREATION_GENERATED` in `House`; or `QUANTITY_TO_EAT` in `Peasant`.
The `Logger` is also quite simple to change to update so mae it yours !!!

---

    ⚠️ Performance Note
    If you try to speed the JavaFX simulation, you'll reduce 'DELTA_TIME_MS' but if you reduce it a lot, as for example 
    to 1ms, your simulation will just crash X﹏X .

---
## Authors 

**Salim Chaoui El Faiz**  
🔗 [GitHub – Salim Chaoui El Faiz](https://github.com/SalimThePokemonMaster)

**Sami Kabbaj**  
🔗 [GitHub – Sami Kabbaj](https://github.com/SamiPro206)

---

## 📄 License

For more details, see the LICENSE file.