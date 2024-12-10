import { CommonModule } from '@angular/common';
import { Component, forwardRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DamLayoutComponent, DamSideBarToggleComponent, DamFullscreenButtonComponent, DamSaveButtonComponent, DamResetButtonComponent, DamAlertsContainerComponent, DamWidgetComponent, DataStateValue, DataStateRepository, selectRouteParams } from '@usnistgov/ngx-dam-framework';
import { CodesetService } from '../../services/codeset.service';
import { ICodeset, ICodesetVersion, ISectionLinkDisplay } from '../../models/codeset';
import { map, Observable, take } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { CodesetSidebarComponent } from '../codeset-sidebar/codeset-sidebar.component';
import { Store } from '@ngrx/store';
import { InputCopyComponent } from '../input-copy/input-copy.component';
import { loadCodeset } from '../../store/codeset.actions';


export const CodesetState = new DataStateValue<ICodeset>({
  key: 'codeset',
  routeLoader: (params, injector) => {
    const dataService = injector.get(CodesetService);
    return dataService.getCodeset(params['codesetId']);
    // const store = injector.get(Store);
    // return store.dispatch(loadCodeset({ codesetId: params['codesetId'] }));
  }
});

export const CodesetVersionsState = new DataStateRepository<ICodesetVersion>({
  name: 'codesetVersions',
  routeLoader: (params, injector) => {
    // const dataService = injector.get(CodesetService);
    // return dataService.getSectionLinkDisplayForEntity(params['codesetId']);
    const store = injector.get(Store);

    return CodesetState.getOneValue(store).pipe(
      map((codeset) => {
        return codeset.versions
      })
    )
  }
});
export const CODESET_WIDGET_ID = 'CODESET_WIDGET_ID';

@Component({
  selector: 'app-codeset-widget',
  standalone: true,
  imports: [
    CommonModule,
    DamLayoutComponent,
    DamSideBarToggleComponent,
    DamFullscreenButtonComponent,
    DamSaveButtonComponent,
    DamResetButtonComponent,
    DamAlertsContainerComponent,
    RouterModule,
    FaIconComponent,
    CodesetSidebarComponent,
    InputCopyComponent

  ],
  providers: [
    {
      provide: DamWidgetComponent,
      useExisting: forwardRef(() => CodesetWidgetComponent)
    },
  ],
  templateUrl: './codeset-widget.component.html',
  styleUrl: './codeset-widget.component.scss'
})
export class CodesetWidgetComponent extends DamWidgetComponent {
  codeset$: Observable<ICodeset>;
  codeSetURL: string = '';
  constructor() {
    super(CODESET_WIDGET_ID);
    this.codeset$ = CodesetState.getValue(this.store);
    this.store.select(selectRouteParams).pipe(
      take(1),
      map((params: Record<string, string>) => {
        const host = window.location.protocol + '//' + window.location.host;
        this.codeSetURL = host + '/codesets/' + params['codesetId']
      })
    ).subscribe()
  }
}
