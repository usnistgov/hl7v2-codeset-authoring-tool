import { Component, computed, Input, model, ModelSignal, signal, Signal, ViewChild, WritableSignal } from '@angular/core';
import { ICodesetVersion, ISectionLink, ISectionLinkDisplay } from '../../models/codeset';
import { combineLatest, flatMap, map, mergeMap, Observable, of, take, tap } from 'rxjs';
import { CodesetState, CodesetVersionsState } from '../codeset-widget/codeset-widget.component';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterModule } from '@angular/router';
import { ConfirmDialogComponent, DamSideBarToggleComponent, MessageHandlerMode, selectRouteParams, UtilityService } from '@usnistgov/ngx-dam-framework';
import { FormsModule } from '@angular/forms';
import { NgbPopover } from '@ng-bootstrap/ng-bootstrap';
import { MatDialog } from '@angular/material/dialog';
import { ContextMenu, ContextMenuModule } from 'primeng/contextmenu';
import { MenuItem } from 'primeng/api';
import { deleteCodesetVersion, loadCodeset } from '../../store/codeset.actions';
import { CodesetService } from '../../services/codeset.service';

@Component({
  selector: 'app-codeset-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    FaIconComponent,
    RouterModule,
    DamSideBarToggleComponent,
    FormsModule,
    NgbPopover,
    ContextMenuModule
  ],
  templateUrl: './codeset-sidebar.component.html',
  styleUrl: './codeset-sidebar.component.scss'
})
export class CodesetSidebarComponent {


  filterText: ModelSignal<string> = model('');
  sections: WritableSignal<ISectionLinkDisplay[]> = signal([]);
  filteredSections!: Signal<ISectionLinkDisplay[]>;
  codesetVersions$: Observable<ICodesetVersion[]>;
  @ViewChild('cm') cm!: ContextMenu;
  options: MenuItem[] = []
  selectedCodesetVersion!: ICodesetVersion | null;
  constructor(private store: Store, private dialog: MatDialog, private utilityService: UtilityService, private codesetService: CodesetService,) {
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

    this.options = [
      {
        label: "Delete",
        icon: 'trash-can',
        command: (event) => {
          console.log(event)
          this.deleteCodeSetVersion()
        }

      }
    ]
  }

  onContextMenu(event: any, codeSetVersion: ICodesetVersion) {
    this.selectedCodesetVersion = codeSetVersion;
    this.cm.target = event.currentTarget;
    this.cm.show(event);
  }
  onHide() {
    // this.selectedCodesetVersion = null;
  }
  deleteCodeSetVersion() {
    if (!this.selectedCodesetVersion) return;

    const confirmDialog$ = this.dialog.open(ConfirmDialogComponent, {
      data: {
        action: 'Delete Code Set Version',
        question: `Are you sure you want to delete this Code Set Version ${this.selectedCodesetVersion.version}?`,
      },
    }).afterClosed();

    const routeParams$ = this.store.select(selectRouteParams).pipe(
      take(1)
    );

    combineLatest([routeParams$, confirmDialog$]).pipe(
      take(1),
      mergeMap(([params, confirmed]: [any, boolean]) => {
        if (confirmed && this.selectedCodesetVersion) {
          return this.utilityService.use(
            this.codesetService.deleteCodesetVersion(params.codesetId, this.selectedCodesetVersion.id),
            {
              loader: {
                blockUI: true
              },
              alert: {
                fromHttpResponse: true,
                mode: MessageHandlerMode.MESSAGE_RESULT_AND_ERROR
              }

            }
          ).pipe(
            map(res => {
              this.selectedCodesetVersion = null;
              CodesetState.getOneValue(this.store).pipe(
                take(1),
                map((codeset) => {
                  this.store.dispatch(loadCodeset({ codesetId: codeset.id, redirect: true }));

                })
              ).subscribe()
            })
          )
        }
        return of();
      })
    ).subscribe();
  }




}
