import { Route } from '@angular/router';
import { ConfigureComponent } from './pages/configure/configure.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { TestingComponent } from './pages/testing/testing.component';

export const appRoutes: Route[] = [
    {
        path: 'configure/:subsystem',
        component: ConfigureComponent
    },
    {
        path: 'dashboard',
        component: DashboardComponent
    },
    {
        path: 'testing',
        component: TestingComponent
    },
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full' 
    }
];
