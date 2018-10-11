package com.apap.tutorial5.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.service.FlightService;
import com.apap.tutorial5.service.PilotService;

@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping(value = "/flight/add/{licenseNumber)", method = RequestMethod.GET)
	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		ArrayList<FlightModel> flight = new ArrayList<>();
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		pilot.setPilotFlight(flight);
		flight.add(new FlightModel());
		
		model.addAttribute("pilot", pilot);
		model.addAttribute("title", "Add Flight");
		return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params= {"save"})
	private String addFlightSubmit(@ModelAttribute PilotModel pilot, Model model) {
		PilotModel pilotModel = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		for (FlightModel f : pilot.getPilotFlight()) {
			f.setPilot(pilotModel);
			flightService.addFlight(f);
		}
		model.addAttribute("title", "APAP");
		return "add";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"addRow"}, method = RequestMethod.POST)
	public String addRow(PilotModel pilot, BindingResult bindingResult, Model model) {
		if (pilot.getPilotFlight() ==  null) {
			pilot.setPilotFlight(new ArrayList<FlightModel>());
		}
		pilot.getPilotFlight().add(new FlightModel());
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"removeRow"}, method = RequestMethod.POST)
	public String removeRow(PilotModel pilot, BindingResult bindingResult, HttpServletRequest request, Model model) {
	   Integer rowId = Integer.valueOf(request.getParameter("removeRow"));
	   pilot.getPilotFlight().remove(rowId.intValue());
	   model.addAttribute("pilot", pilot);
	   return "addFlight";
	}
	
	@RequestMapping(value = "/flight/delete", method = RequestMethod.POST)
	private String delete(@ModelAttribute PilotModel pilot, Model model) {
		for (FlightModel flight : pilot.getPilotFlight()) {
			flightService.deleteFlightByID(flight.getId());
		}
		model.addAttribute("title", "APAP");
		return "deletePilot";
	}
	
	@RequestMapping(value = "/flight/update/{licenseNumber}/{flightID}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "licenseNumber") String licenseNumber, @PathVariable(value = "flightID") long flightID, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		FlightModel flight = flightService.getFlightByID(flightID);
		
		model.addAttribute("pilot", pilot);
		model.addAttribute("flight", flight);
		model.addAttribute("title", "Update Flight");
		return "updateFlight";
	}
	
	@RequestMapping(value = "/flight/update", method = RequestMethod.POST)
	private String updateFlightSubmit(@ModelAttribute FlightModel flight, Model model) {
		flight.setFlightNumber(flight.getFlightNumber());
		flight.setOrigin(flight.getOrigin());
		flight.setDestination(flight.getDestination());
		flight.setTime(flight.getTime());
		flightService.addFlight(flight);
		model.addAttribute("title", "APAP");
		return "update";
	}
	
	@RequestMapping(value = "/flight/view")
	public String view(@RequestParam("flightNumber") String flightNumber, Model model) {
		ArrayList<FlightModel> allFlightSesuai = new ArrayList<>();
		List<PilotModel> allPilot = pilotService.findAll();
		for (PilotModel pilot:allPilot) {
			for (FlightModel flight:pilot.getPilotFlight()) {
				if (flight.getFlightNumber().equals(flightNumber)) {
					allFlightSesuai.add(flight);
				}
			}
		}
		model.addAttribute("allFlightSesuai", allFlightSesuai);
		model.addAttribute("title", "View Flight");
		return "view-flight";
	}

}
