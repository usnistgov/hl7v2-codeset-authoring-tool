import { CommonModule } from '@angular/common';
import { Component, forwardRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DamLayoutComponent, DamSideBarToggleComponent, DamFullscreenButtonComponent, DamSaveButtonComponent, DamResetButtonComponent, DamAlertsContainerComponent, DamWidgetComponent, DataStateValue, DataStateRepository } from '@usnistgov/ngx-dam-framework';
import { EntityService } from '../../services/entity.service';
import { IEntity, ISectionLinkDisplay } from '../../models/entity';
import { Observable } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { EntitySidebarComponent } from '../entity-sidebar/entity-sidebar.component';


export const EntityState = new DataStateValue<IEntity>({
  key: 'entity',
  routeLoader: (params, injector) => {
    const dataService = injector.get(EntityService);
    return dataService.getEntityValue(params['entityId']);
  }
});
export const SectionLinkDisplayState = new DataStateRepository<ISectionLinkDisplay>({
  name: 'sectionLinkDisplay',
  routeLoader: (params, injector) => {
    const dataService = injector.get(EntityService);
    return dataService.getSectionLinkDisplayForEntity(params['entityId']);
  }
});
export const ENTITY_WIDGET_ID = 'ENTITY_WIDGET_ID';

@Component({
  selector: 'app-entity-widget',
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
    EntitySidebarComponent
  ],
  providers: [
    {
      provide: DamWidgetComponent,
      useExisting: forwardRef(() => EntityWidgetComponent)
    },
  ],
  templateUrl: './entity-widget.component.html',
  styleUrl: './entity-widget.component.scss'
})
export class EntityWidgetComponent extends DamWidgetComponent {
  entity$: Observable<IEntity>;

  constructor() {
    super(ENTITY_WIDGET_ID);
    this.entity$ = EntityState.getValue(this.store)
  }
}
