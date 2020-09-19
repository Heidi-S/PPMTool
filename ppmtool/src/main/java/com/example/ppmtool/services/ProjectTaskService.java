package com.example.ppmtool.services;

import org.springframework.stereotype.Service;
import com.example.ppmtool.repositories.ProjectTaskRepository;
import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.domain.Backlog;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ppmtool.exceptions.ProjectNotFoundException; 


@Service
public class ProjectTaskService {
	
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		

		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
				//backlogRepository.findByProjectIdentifier(projectIdentifier);
		
		projectTask.setBacklog(backlog);
		
		Integer BacklogSequence = backlog.getPTSequence();
		BacklogSequence++;
		backlog.setPTSequence(BacklogSequence);
		
		projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
	
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
			projectTask.setStatus("TO_DO");
		}
		
		return projectTaskRepository.save(projectTask);

				
		
	}
	
	public Iterable<ProjectTask>findBacklogById(String id, String username){
		
		projectService.findProjectByIdentifier(id, username);

		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		projectService.findProjectByIdentifier(backlog_id, username);

		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist");
		}
		
		if (!projectTask.getProjectIdentifier().contentEquals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist in Project: "+backlog_id);
		}
		return projectTaskRepository.findByProjectSequence(pt_id);
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);		
		projectTaskRepository.delete(projectTask);
	}
	

	
	
	
	

}
