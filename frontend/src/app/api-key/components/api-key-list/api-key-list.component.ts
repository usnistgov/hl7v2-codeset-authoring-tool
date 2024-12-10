import { Component, inject } from '@angular/core';
import {
  AlertService,
  DamAlertsContainerComponent,
  DataStateRepository,
  IListItemControl,
  ListWidgetComponent,
  MessageType,
  SortOrder,
  UtilityService,
} from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { map, Observable, of, skip, switchMap, tap, throwError } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  Router,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { ApiKeyService } from '../../services/api-key.service';
import { IAPIKeyDisplay } from '../../models/api-key';
import { TableModule } from 'primeng/table';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import moment from 'moment';
import { ICodesetDescriptor } from '../../../codeset/models/codeset';
import { loadApiKeys } from '../../store/api-key.actions';

export const ApiKeyListState = new DataStateRepository<IAPIKeyDisplay>({
  name: 'apiKeyList', // unique name for this state variable
  routeLoader: (params, injector, state) => {
    const apiKey = injector.get(ApiKeyService);
    return apiKey.getApiKeys();
  },
});

@Component({
  selector: 'app-api-key-list',
  standalone: true,
  imports: [
    CommonModule,
    ListWidgetComponent,
    RouterLink,
    RouterLinkActive,
    FormsModule,
    TableModule,
    FaIconComponent,
    DamAlertsContainerComponent,
    ConfirmDialogModule,

  ],
  providers: [ConfirmationService],
  templateUrl: './api-key-list.component.html',
  styleUrl: './api-key-list.component.scss',
})
export class ApiKeyListComponent {
  apiKeyList = ApiKeyListState;
  apiKey$: Observable<IAPIKeyDisplay[]>;
  openedApiKey: string = '';

  constructor(
    private store: Store,
    private routeSnapshot: ActivatedRoute,
    private apiKeyService: ApiKeyService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private utilityService: UtilityService,
  ) {
    this.apiKey$ = ApiKeyListState.findAll(this.store)

  }

  ngOnInit(): void {

  }
  createApiKey() {
    this.router.navigate(['/api-keys/create'])
  }
  expiresIn(date: Date) {
    const a = (moment as any)(date);
    const b = (moment as any)(new Date());
    return a.from(b);
  }
  deleteAPIKey(key: IAPIKeyDisplay) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete API Key "' + key.name + '", you will not be able to use the key to access resources anymore',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.utilityService.useLoaderWithErrorAlert(
          this.apiKeyService.deleteApiKey(key.id),
          {
            loader: {
              blockUI: true
            }
          }
        ).pipe(
          tap((message) => {
            this.store.dispatch(loadApiKeys());
          }),
        ).subscribe();

      },
      reject: () => {
      },
      key: "positionDialog"
    });
  }
}
