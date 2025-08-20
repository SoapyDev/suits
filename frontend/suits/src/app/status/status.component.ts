import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {MatTooltip} from '@angular/material/tooltip';
import {NgStyle} from '@angular/common';

@Component({
  selector: 'app-status',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatTooltip,
    NgStyle,
  ],
  templateUrl: './status.component.html',
  styleUrl: './status.component.scss'
})
export class StatusComponent {

  protected statusList = [
    {value: "NON_DEBUTE",name:"Non débutée"},
    {value: "EN_ATTENTE", name:"En attente"},
    {value: "EN_COURS", name:"En cours"},
    {value: "TERMINEE",name : "Terminée"}
  ];
  @Input()  status! : string;
  @Input() readonly!: boolean;
  @Output() updateStatus = new EventEmitter<string>();

  public  getColor(status: string){
    switch (status){
      case "NON_DEBUTE": return { color: '#999999' };
      case "EN_ATTENTE": return { color: '#DA954B' };
      default: return { color: '#176F34' };
    }
  }

  public isCompleted(status: string) : boolean {
    return status === "TERMINEE";
  }
  public isWaiting(status: string) : boolean {
    return status === "EN_ATTENTE";
  }

  public updateStatusEvent(status: string) {
    this.status = status;
    this.updateStatus.emit(status);
  }
}
