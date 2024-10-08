/**
 * 
 */
package es.ull.simulation.examples.hospital;

import es.ull.simulation.functions.TimeFunctionFactory;
import es.ull.simulation.condition.PercentageCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.ExclusiveChoiceFlow;

/**
 * The model of the hospital to be simulated.
 * 
 * The "hospital" is simply a set of operating theatres and consultation rooms. 
 * Patients arrive at the hospital to be seen by a doctor. If the doctor decides so, a surgical team operates on
 * the patient. After the surgical intervention, the doctor assesses the result of the intervention.
 * In case the intervention failed, the surgical team reoperates on the patient, and further reassessments
 * are performed.
 * 
 * The hospital has 6 doctors (3 of them are also surgeons), and 3 nurses. Doctors and nurses work from 8:00 to 15:00. 
 * Surgical interventions are scheduled from 11:00 to 14:00.
 * 
 * 5% of the patients that attend to an appointment require surgery, independently of the previous history
 * of the patient.
 * 
 * Surgical teams require 2 surgeons and 1 nurse. In some cases, only 1 surgeon and 1 nurse may carry out the surgical 
 * intervention, but only by incrementing its duration.
 *  
 * @author Iván Castilla Rodríguez
 *
 */
public class BasicHospitalModel extends Simulation {

	/**
	 * @param id
	 * @param unit
	 * @param startTs
	 * @param endTs
	 */
	public BasicHospitalModel(int id) {
		super(id, "Hospital");
		// Define the model
		
		// The only element type: patients
		ElementType etPatient = new ElementType(this, "Patient");
		
		// The three resource types involved in the simulation
		ResourceType rtDoctor = new ResourceType(this, "Doctor");
		ResourceType rtSurgeon = new ResourceType(this, "Surgeon");
		ResourceType rtNurse = new ResourceType(this, "Nurse");
		
		// Create the specific resources
		Resource resDoctor1 = new Resource(this, "Dr. J. Martin");
		Resource resDoctor2 = new Resource(this, "Dr. A. Martin");
		Resource resDoctor3 = new Resource(this, "Dr. B. Johnson");
		Resource resDoctor4 = new Resource(this, "Dr. J. Wolf");
		Resource resDoctor5 = new Resource(this, "Dr. P. Lass");
		Resource resDoctor6 = new Resource(this, "Dr. O.J. Simpson");
		Resource resNurse1 = new Resource(this, "L. Boss");
		Resource resNurse2 = new Resource(this, "A. Brown");
		Resource resNurse3 = new Resource(this, "H. Clinton");
		
		// Define the work timetables
		// ... for doctors and nurses (starting at 8:00)
		SimulationPeriodicCycle docCycle = SimulationPeriodicCycle.newDailyCycle(unit, 8 * 60);
		// .. for surgeons (starting at 11:00)
		SimulationPeriodicCycle surgeonCycle = SimulationPeriodicCycle.newDailyCycle(unit, 11 * 60);
		
		// Define the roles of the resources
		resDoctor1.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor2.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor3.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor4.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor5.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor6.newTimeTableOrCancelEntriesAdder(rtDoctor).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resNurse1.newTimeTableOrCancelEntriesAdder(rtNurse).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resNurse2.newTimeTableOrCancelEntriesAdder(rtNurse).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resNurse3.newTimeTableOrCancelEntriesAdder(rtNurse).withDuration(docCycle, 7 * 60).addTimeTableEntry();
		resDoctor1.newTimeTableOrCancelEntriesAdder(rtSurgeon).withDuration(surgeonCycle, 3 * 60).addTimeTableEntry();
		resDoctor2.newTimeTableOrCancelEntriesAdder(rtSurgeon).withDuration(surgeonCycle, 3 * 60).addTimeTableEntry();
		resDoctor3.newTimeTableOrCancelEntriesAdder(rtSurgeon).withDuration(surgeonCycle, 3 * 60).addTimeTableEntry();

		
		// Create the activities
		ActivityFlow actAppointment = new ActivityFlow(this, "Appointment");
		ActivityFlow actSurgery = new ActivityFlow(this, "Surgery");
		
		// Define the workgroups
		WorkGroup wgAppointment = new WorkGroup(this, rtDoctor, 1);
		WorkGroup wgSurgery1 = new WorkGroup(this, new ResourceType[] {rtSurgeon, rtNurse}, new int[] {2, 1});
		WorkGroup wgSurgery2 = new WorkGroup(this, new ResourceType[] {rtSurgeon, rtNurse}, new int[] {1, 1});
		
		// Assign duration and workgroups to activities
		actAppointment.newWorkGroupAdder(wgAppointment).withDelay(TimeFunctionFactory.getInstance(
				"UniformVariate", 7, 10)).add();
		actSurgery.newWorkGroupAdder(wgSurgery1).withDelay(40L).add();
		actSurgery.newWorkGroupAdder(wgSurgery2).withDelay(60L).add();
		
		// Create a conditional IFlow to determine if a patient requires surgery
		ExclusiveChoiceFlow fRequireSurgery = new ExclusiveChoiceFlow(this);
		// Define 5% of patients requiring surgery
		PercentageCondition<ElementInstance> requiresSurgeryCondition = new PercentageCondition<ElementInstance>(50.0);
		
		actAppointment.link(fRequireSurgery);
		fRequireSurgery.link(actSurgery, requiresSurgeryCondition);
		actSurgery.link(actAppointment);
		
		new TimeDrivenElementGenerator(this, 1, etPatient, actAppointment, docCycle);
	}

}
