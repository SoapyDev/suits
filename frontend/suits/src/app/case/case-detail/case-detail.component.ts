import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, OnInit, ViewChild} from '@angular/core';
import {Case} from '../models/case.model';
import {StatusComponent} from '../../status/status.component';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable, MatTableDataSource,
} from '@angular/material/table';
import {MatButton, MatFabButton, MatIconAnchor, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/divider';
import {SiteNavPickerComponent} from '../../site-nav-picker/site-nav-picker.component';
import {SiteNavComponent} from '../../site-nav/site-nav.component';
import {priorityToString} from '../../task/models/priority.enum';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {MatTooltip} from '@angular/material/tooltip';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {MemberDialogComponent} from '../../member-dialog/member-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {Task} from '../../task/models/task.model';
import {TaskService} from '../../task/service/task.service';
import {ActivatedRoute} from '@angular/router';
import {CaseService} from '../services/case.service';
import {TeamMembers} from '../../home/model/team-members.model';
import {DocumentService} from '../../document/service/document.service';
import {Document} from '../../document/models/document.model';
import {MatFormField} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatInput} from '@angular/material/input';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ConventionService} from '../services/convention.service';
import {ConventionHonoraires, ConventionHonorairesRequest, ConventionId} from '../models/convention.model';
import {ConventionFormComponent} from '../convention-form/convention-form.component';
import {DatePipe} from '@angular/common';
import {MatProgressBarModule} from '@angular/material/progress-bar';

@Component({
  selector: 'app-case-detail',
  imports: [
    StatusComponent,
    MatTable,
    MatIconButton,
    MatIcon,
    MatDivider,
    SiteNavPickerComponent,
    SiteNavComponent,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatIconAnchor,
    MatTooltip,
    MatSort,
    MatSortHeader,
    NgIf,
    MatFormField,
    FormsModule,
    MatButton,
    MatInput,
    MatFabButton,
    MatProgressSpinner,
    NgOptimizedImage,
    ConventionFormComponent,
    DatePipe,
    MatProgressBarModule
  ],
  templateUrl: './case-detail.component.html',
  styleUrl: './case-detail.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
})
export class CaseDetailComponent implements OnInit{


  private _snackBar = inject(MatSnackBar);
  protected readonly priorityToString = priorityToString;
  protected taskColumns = ["statut","name", "assignee", "start", "finish", "priority", "see-task"];
  protected docColumns = ["title", "description", "type", "uploadDate", "uploadedBy", "open-doc", "delete-doc"];
  private maxFileSize: number = 20000000

  protected getTaskLink(id : number) {
    return `/app/tasks/${id}`;
  }

  protected caseDetail : Case = {
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    civicNumber: '',
    streetName: '',
    city: '',
    province: '',
    postalCode: '',
    birthdate: '',
    description: '',
    status: '',
    startOfMandate: new Date(),
    endOfMandate: new Date(),
    isClosed: false,
    lawField: '',
    agreement: '',
    legalFileTeamMembers: []
  };

  protected tasks: Task[] = [];
  protected isLoading: boolean = true;
  protected caseId: string = "";
  protected selectedFile!: File | null;
  protected fileDescription: string = "";
  protected isFileSelected: boolean = false;
  protected isFileSubmitted: boolean = false;
  protected isFileTransmitted: boolean = false;
  protected docAlreadyExists: boolean = false;
  protected taskDataSource!: MatTableDataSource<Task>;
  protected docDataSource!: MatTableDataSource<Document>;
  @ViewChild(MatSort) sort!: MatSort;

  protected conventionHonoraires: ConventionHonoraires | null = {
    id: -1,
    legalFile: {
      id: '',
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      civicNumber: '',
      streetName: '',
      city: '',
      province: '',
      postalCode: '',
      birthdate: '',
      description: '',
      status: '',
      startOfMandate: new Date(),
      endOfMandate: new Date(),
      isClosed: false,
      lawField: '',
      agreement: '',
      legalFileTeamMembers: []
    },
    agreement: '',
    dateEntente: new Date,
    mandat: '',
    tarifHoraire: -1,
    estimationPrix: -1,
    creator: '',
    isApproved: true,
    approvalDate: new Date(),
  }

  protected conventionHonorairesRequest: ConventionHonorairesRequest = {
    legalFileId: '',
    agreement: '',
    mandat: '',
    tarifHoraire: -1,
    estimationPrix: -1,
  }

  protected showConventionForm = false;
  protected role: string | null = '';
  protected billedAmount: number = 0;
  protected billingRatio: number = this.billedAmount/this.conventionHonoraires!.estimationPrix;

  constructor(
    private route: ActivatedRoute,
    protected caseService: CaseService,
    private taskService: TaskService,
    private documentService: DocumentService,
    private conventionService: ConventionService,) {
  }

  ngOnInit(): void {
    this.caseId = this.route.snapshot.paramMap.get('documentId')!;

    this.taskDataSource = new MatTableDataSource(this.tasks);
    this.loadCase(this.caseId).then();
    this.loadTasks(this.caseId).then();
    this.loadDocuments();
    this.determineRole();

  }

  private async loadCase(caseId: string) {
    try{
      this.caseDetail = await this.caseService.getCaseByID(caseId);
      this.conventionHonoraires = await this.conventionService.getConventionHonoraires(caseId);
      if(this.conventionHonoraires != null){
        this.conventionHonorairesRequest.mandat = this.conventionHonoraires.mandat;
        this.conventionHonorairesRequest.agreement = this.conventionHonoraires.agreement;
        this.conventionHonorairesRequest.legalFileId = caseId;
        this.conventionHonorairesRequest.estimationPrix = this.conventionHonoraires.estimationPrix;
        this.conventionHonorairesRequest.tarifHoraire = this.conventionHonoraires.tarifHoraire;
        this.updateBillingRatio();
      } else {
        this.conventionHonorairesRequest.mandat = '';
        this.conventionHonorairesRequest.agreement = '';
        this.conventionHonorairesRequest.legalFileId = caseId;
        this.conventionHonorairesRequest.estimationPrix = 0;
        this.conventionHonorairesRequest.tarifHoraire = 0;
      }
    }catch (error){
      console.error(error);
      this._snackBar.open("Erreur lors de la récupération du dossier", "OK");
    }
  }

  private async loadTasks(caseId: string): Promise<void> {

    if (caseId) {
      try {
        this.tasks = await this.taskService.getTaskByCaseId(caseId);
        this.taskDataSource.data = this.tasks;
        this.isLoading = false;
        await this.loadBilledAmount();
        this.updateBillingRatio();

      } catch (error) {
        console.log("Erreur",error);
        this._snackBar.open('Erreur lors de la récupération des tâches.', "OK");
        this.isLoading = false;
      }
    } else {
      this._snackBar.open("Le ID du dossier n'a pas pu être récupéré", "OK");
      this.isLoading = false;
    }



  }

  private loadDocuments(): void {

    this.documentService.getDocuments(this.caseId).subscribe({
      next: (documents) => {
        this.docDataSource = new MatTableDataSource(documents);
        this.docDataSource.sort = this.sort;
      },
      error: (err) => {
        this._snackBar.open('Erreur lors du chargement des documents', "OK");
        console.error("Erreur lors du chargement des documents :", err);
      }
    });
  }

  private async loadBilledAmount(){
    this.billedAmount = 0;
    if( this.tasks !==null && this.tasks.length != 0){
      this.tasks.forEach(task => {
        task.taskTimes!.forEach(time =>{
          let amount = time.time * time.tarification;
          this.billedAmount += amount;
        })
      });
    }
  }

  private updateBillingRatio() {
    if (this.conventionHonoraires && this.conventionHonoraires.estimationPrix > 0) {
      this.billingRatio =Math.floor(this.billedAmount / this.conventionHonoraires.estimationPrix *100);
      if(this.billingRatio > 100){
        this.billingRatio = 100;
      }
    } else {
      this.billingRatio = 0;
    }
  }

  protected updateCaseStatus(status: string){
    try{
      this.caseDetail.status = status;
      this.caseService.updateCase(this.caseDetail).then( );
    }catch (error){
      this._snackBar.open('Erreur lors de la mise à jour du dossier.', "OK");
    }
  }

  ngAfterViewInit() {
    this.taskDataSource.sort = this.sort;
  }

  removeMember(email: string) {
    this.caseDetail.legalFileTeamMembers = this.caseDetail.legalFileTeamMembers.filter(teamMember => teamMember.username !== email);
  }

  readonly dialog = inject(MatDialog);

  openMemberDialog(): void {

    const dialogRef = this.dialog.open(MemberDialogComponent, {
      width: '100%',
      maxWidth: '600px',
      height: "100%",
      maxHeight: '300px',
      data: this.caseDetail.legalFileTeamMembers
    })

    dialogRef.afterClosed().subscribe((result: TeamMembers[] | undefined) => {
      if (result) {
        this.caseService.updateCaseTeamMembers(this.caseId, result).then();
      }
    });

  }

  formatStringDate(dateString: string | null | undefined): string {
    if (!dateString) return 'Date inconnue';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Date invalide';

    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }

  protected agreementValue(agreement : string){
    if (agreement === 'HORAIRE') return 'Horaire'
    if (agreement === 'POURCENTAGE') return 'Pourcentage'
    return 'Forfaitaire'
  }

  protected lawFieldValue(lawfield : string){
    if (lawfield === 'PENAL') return 'Pénal';
    if (lawfield === 'FAMILIAL') return 'Famille';
    if (lawfield === 'CIVIL') return 'Civil';
    if (lawfield === 'ADMINISTRATIF') return 'Administratif';
    if (lawfield === 'CONSTITUTIONNEL') return 'Constitutionnel';
    if (lawfield === 'LOGEMENT') return 'Logement';
    if (lawfield === 'JEUNESSE') return 'Jeunesse';

    return 'Travail';
  }


  async createNewTask() {
    let caseId: string = "";
    this.route.paramMap.subscribe(params => {
      caseId = params.get('documentId') || '';
    });

    try {
      await this.taskService.createTask(caseId);

    }catch (error){
      this._snackBar.open('Error lors de la création de la tâche', "OK");
      console.error("Error lors de la création de la tâche : ", error);
    }

  }

  async updateTaskStatus(task : Task, status: string) {
    try{
      await this.taskService.patchTask(task.id!.toString(), {'status':  status});
    }catch (error){
      this._snackBar.open('Error lors de la mise à jour de la tâche', "OK");
      console.error("Error lors de la mise à jour de la tâche : ", error);
    }
  }

  onFileSelected(event: Event) {
    this.docAlreadyExists = false;
    this.isFileTransmitted = false;
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
      this.isFileSelected = true;
    }
  }

  uploadDocument() {
    if (!this.selectedFile) return;

    const alreadyExists = this.docDataSource?.data.some(doc => doc.name === this.selectedFile!.name);

    if (alreadyExists) {
      this.isFileSelected = false;
      this.selectedFile = null;
      this.fileDescription = '';
      this.docAlreadyExists = true;
      this._snackBar.open('Un document avec ce nom existe déjà.', "OK");

      return;
    }

    if (this.selectedFile.size > this.maxFileSize) {
      this.isFileSelected = false;
      this.selectedFile = null;
      this.fileDescription = '';
      this._snackBar.open('La taille du document doit être inférieur à 20Mo', "OK");

      return;
    }

    this.documentService.uploadDocument(this.caseId, this.fileDescription, this.selectedFile)
      .subscribe({
        next: () => {
          this.isFileSubmitted = true;
        },
        error: (err) => {
          this._snackBar.open(`Erreur pendant le téléversement`, "OK");
          console.error('Erreur pendant le téléversement :', err);
        },
        complete: () => {
          this.isFileSubmitted = false;
          this.isFileTransmitted = true;
          this.isFileSelected = false;
          this.selectedFile = null;
          this.fileDescription = '';
          this.loadDocuments();
          this._snackBar.open(`Document téléversé!`, "OK");
        }
      });

  }

  deleteDocument(docId: number){

    this.documentService.deleteFile(this.caseId, docId).subscribe({
      next: () => {
        this.docDataSource.data = this.docDataSource.data.filter(doc => doc.id !== docId);
        this._snackBar.open(`Document supprimé!`, "OK");

      },
      error: (error) => {
        this._snackBar.open(`Erreur lors de la suppression`, "OK");
        console.error('Erreur lors de la suppression :', error);
      },
      complete: () => {

      }
    });
  }

  getFileIcon(filename: string): string {
    const ext = filename.split('.').pop()?.toLowerCase();
    switch (ext) {
      case 'pdf': return '/pdf.png';
      case 'doc':
      case 'docx': return '/doc.png';
      case 'txt': return '/document.png';
      case 'jpg':
      case 'jpeg':
      case 'webp':
      case 'avif':
      case 'png': return '/image.png';
      case 'mp4':
      case 'mov':
      case 'mkv': return '/video.png';
      default: return '/document.png';
    }
  }

  openConventionForm(){
    this.showConventionForm = true;
  }

  async submitConventionFormData(conventionHonorairesRequest: ConventionHonorairesRequest) {
    this.showConventionForm = false;
    conventionHonorairesRequest.legalFileId = this.caseDetail.id;
    if (this.conventionHonoraires == null) {
      this.conventionHonoraires = await this.conventionService.createConventionHonoraires(conventionHonorairesRequest);
    } else {
      this.conventionHonoraires = await this.conventionService.modifyConventionHonoraire(conventionHonorairesRequest);
    }
    this.onConventionHonorairesUpdate();
  }

  closeConventionForm(){
    this.showConventionForm = false;

  }

  onConventionHonorairesUpdate(){
    if(this.conventionHonoraires != null) {
      this.conventionHonorairesRequest.mandat = this.conventionHonoraires.mandat;
      this.conventionHonorairesRequest.agreement = this.conventionHonoraires.agreement;
      this.conventionHonorairesRequest.tarifHoraire = this.conventionHonoraires.tarifHoraire;
      this.conventionHonorairesRequest.estimationPrix = this.conventionHonoraires.estimationPrix;
      this.conventionHonorairesRequest.legalFileId = this.conventionHonoraires.legalFile.id;
      this.updateBillingRatio();
    }

  }

  async onConventionApproval(){
    let conventionId: ConventionId = {
      id : this.conventionHonoraires!.id
    };
    this.conventionHonoraires = await this.conventionService.acceptConventionHonoraires(conventionId);
  }

  determineRole(): void {
       this.role = localStorage.getItem('role') ;
  }

}
