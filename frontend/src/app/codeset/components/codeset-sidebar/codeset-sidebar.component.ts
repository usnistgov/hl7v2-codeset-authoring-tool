import { Component, computed, Input, model, ModelSignal, signal, Signal, WritableSignal } from '@angular/core';
import { ICodesetVersion, ISectionLink, ISectionLinkDisplay } from '../../models/codeset';
import { combineLatest, map, Observable, take, tap } from 'rxjs';
import { CodesetVersionsState } from '../codeset-widget/codeset-widget.component';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterModule } from '@angular/router';
import { DamSideBarToggleComponent } from '@usnistgov/ngx-dam-framework';
import { FormsModule } from '@angular/forms';
import { NgbPopover } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-codeset-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    FaIconComponent,
    RouterModule,
    DamSideBarToggleComponent,
    FormsModule,
    NgbPopover
  ],
  templateUrl: './codeset-sidebar.component.html',
  styleUrl: './codeset-sidebar.component.scss'
})
export class CodesetSidebarComponent {


  filterText: ModelSignal<string> = model('');
  sections: WritableSignal<ISectionLinkDisplay[]> = signal([]);
  filteredSections!: Signal<ISectionLinkDisplay[]>;
  codesetVersions$: Observable<ICodesetVersion[]>;

  constructor(private store: Store) {
    this.codesetVersions$ = CodesetVersionsState.findAll(this.store)

    this.filteredSections = computed(() => {
      const text = this.filterText();
      const sections = this.sections();
      if (text) {
        return sections.filter((section) => section.label && section.label.toLowerCase().includes(text.toLowerCase()));
      } else {
        return sections;
      }
    });
  }

}
