export function addNewLesson(id, name, teacher,t_id){
	document.getElementById(t_id).innerHTML +=' <div class="col-md-4" > <div class="card cardv p-3 mb-2"> <div class="d-flex justify-content-between"> <div class="d-flex flex-row align-items-center"> <div class="icon"> <i class="bx bxl-mailchimp"></i> </div> <div class="ms-2 c-details"> <h6 class="mb-0" ></h6> <span>'+teacher+'</span> </div> </div> <div class="badge"> <span>Lesson</span> </div> </div> <div class="mt-5"> <a style="font-size:19px" class="h2" th:href="@{/lezione/ '+ id +'}"  >'+ name +'</a> <div class="mt-5"> <div class="progress"> <div class="progress-bar" role="progressbar" style="width:50%" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div> </div> </div> </div> </div> </div> '
	
}