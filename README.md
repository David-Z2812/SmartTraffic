# SmartTraffic

## Introduction

In this project, we developed the Smart Traffic service, aimed at creating an intelligent traffic management system as well as the navigation of an autonomous car within this system. Building upon concepts studied during previous seminars, we designed and developed solutions based on object communication, primarily using the MQTT protocol. The goal is to create autonomous vehicles that can navigate through a circuit and adapt dynamically to various parameters related to other vehicles and the environment, responding in real-time to events.

This document details the objectives, methodology, and results of the project. It outlines the design and development stages of each component, as well as their integration into the Smart Traffic ecosystem.

For this lab, we use the following road map (figure). The infrastructure is designed to enable both vehicles and roads to remain connected and continuously exchange information.

## How to Use

### 1. Run the TestScenarioStarter

To start the program, execute the `TestScenarioStarter` file. (.\smartcar\src\smartcar\starter\TestScenarioStarter.java)
This file serves as the main entry point of the application, initializing all necessary components such as vehicles, routes, panels, and the simulator. It orchestrates the simulation by defining routes, assigning them to vehicles, setting up SmartRoads and Panels, and managing the simulation loop, including events like accidents and synchronization of all components.

Simply run the file to launch the entire Smart Traffic system and start the simulation.

### 2. Alternatively, configure VS Code with `launch.json`

You can also configure Visual Studio Code (VS Code) to run the program using the following `launch.json` configuration:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "TestScenarioStarter",
            "request": "launch",
            "mainClass": "smartcar.starter.TestScenarioStarter",
            "projectName": "smartcar"
        }
    ]
}
