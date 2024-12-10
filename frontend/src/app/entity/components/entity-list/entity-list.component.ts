import { Component, inject } from '@angular/core';
import { AlertService, DataStateRepository, IListItemControl, ListWidgetComponent, MessageType, SortOrder } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { map, of, skip, switchMap, tap, throwError } from 'rxjs';
import { IEntityDescriptor } from '../../models/entity';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { EntityService } from '../../services/entity.service';

// Initializer for Entity List
export const EntityListState = new DataStateRepository<IEntityDescriptor>({
  name: 'entityList', // unique name for this state variable
  routeLoader: (params, injector, state) => {
    // Hint (Error) : to throw an error while loading the state return an error observable with the error IMessage
    // return throwError(() => ({
    //   status: MessageType.FAILED,
    //   text: 'Could not load entity list',
    // }));

    const entity = injector.get(EntityService);
    return entity.getEntityList(state.route.queryParams['type']);
  }
});

@Component({
  selector: 'app-entity-list',
  standalone: true,
  imports: [
    CommonModule,
    ListWidgetComponent,
    RouterLink,
    RouterLinkActive,
    FormsModule,
  ],
  templateUrl: './entity-list.component.html',
  styleUrl: './entity-list.component.scss'
})
export class EntityListComponent {
  entityList = EntityListState;
  route = inject(ActivatedRoute);
  router = inject(Router);
  sortOrder = SortOrder.ASC;
  controls: IListItemControl<IEntityDescriptor>[] = [{
    key: 'open',
    label: 'Open',
    btnClass: 'btn btn-sm btn-primary',
    iconClass: "bi bi-enter",
    disabled: () => false,
    hidden: () => false,
    onClick: (item: IEntityDescriptor) => {
      this.router.navigate(["/", "entity", item.id]);
    }
  }]
  sort: (field: string | undefined, order: SortOrder) => (a: IEntityDescriptor, b: IEntityDescriptor) => number = (sortField, sortOrder) => (a, b) => {
    const multiplier = sortOrder === SortOrder.ASC ? 1 : -1;
    switch (sortField) {
      case 'name':
        return (a.label.toLowerCase() < b.label.toLowerCase() ? -1 : 1) * multiplier;
    }
    return 0;
  }
  sortOptions = [{
    name: 'Name',
    key: 'name',
  }];

  sortOrderToggle = SortOrder.ASC;


  constructor(
    private store: Store,
    private routeSnapshot: ActivatedRoute,
    private entityService: EntityService
  ) { }

  ngOnInit(): void {
    this.routeSnapshot.queryParams.pipe(
      skip(1),
      switchMap((queryParams) => {
        return this.entityService.getEntityList(queryParams['type']).pipe(
          tap((values) => {
            EntityListState.setValue(this.store, values);
          })
        );
      })
    ).subscribe();
  }
}
