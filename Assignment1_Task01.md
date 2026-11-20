# Task 1

Identify inputs (registration, vehicle type, user/pass, timestamps, capacities) and outputs (ticket, slot, occupancy, fee). Core objects: Vehicle hierarchy, ParkingTicket, Facility, ParkingPass, PricingRule. A vehicle object groups state and behavior; parallel arrays require synchronized indexes. Object model is easier to extend, with O(n) storage.
