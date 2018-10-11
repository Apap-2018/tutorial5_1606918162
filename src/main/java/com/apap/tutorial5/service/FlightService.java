package com.apap.tutorial5.service;

import com.apap.tutorial5.model.FlightModel;

public interface FlightService {
	
	void addFlight(FlightModel flight);
	void deleteFlightByID(long id);
	FlightModel getFlightByID(long id);
	

}
