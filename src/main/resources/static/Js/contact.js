/**
 * 
 */
console.log("Contacts");
const baseURL="http://localhost:8081";
const viewContactModal=document.getElementById('view_contact_modal');

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal=new Modal(viewContactModal,options,instanceOptions);

function openContactModal(){
	contactModal.show();
}

function closeContactModal(){
	
	contactModal.hide();
}

async function loadContactData(id) { 
	
	console.log(id);
	
try{
	const data= await (await fetch(`${baseURL}/api/contacts/${id}`)
		).json();
		console.log(data);
		document.getElementById("contact_name").textContent = data.name || "N/A";
		document.getElementById("contact_email").textContent = data.email || "N/A";
		document.getElementById("contact_phone").textContent = data.phoneNumber || "N/A";
		document.getElementById("contact_address").textContent = data.address || "N/A";
		document.getElementById("contact_description").textContent = data.description || "N/A";
		document.getElementById("contact_linkedIn").textContent = data.linkedIn || "N/A";
		
		

		const imgEl = document.getElementById("contact_image");
		   imgEl.src = data.picture && data.picture.trim() !== ""
		     ? data.picture
		     : "https://cdn-icons-png.flaticon.com/512/149/149071.png";
		
openContactModal();
}catch(error){
	
	console.log("Error loading:",error);
}

	
 }
 
 //delete contact
 async function deleteContact(id) {
   Swal.fire({
     title: "Do you want to delete the Contact?",
     icon: "warning",
     showCancelButton: true,
     confirmButtonText: "Delete",
     cancelButtonText: "Cancel",
     confirmButtonColor: "#dc2626", // red-600
     cancelButtonColor: "#6b7280",  // gray-500
     background: "white",
     color: "black",
   }).then((result) => {
     if (result.isConfirmed) {
       const url = `${baseURL}/user/contacts/delete/${id}`;
       window.location.replace(url);
     }
   });
 }




