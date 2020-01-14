import React from 'react';
import { Tab, Tabs, Nav, Col, Row, Table, InputGroup, FormControl, ListGroup } from 'react-bootstrap'
import DayView from './DayView';

export default function WeekView({ recipes, onChange, week, saveWeek, deleteWeek, }) {
    const createTabLinks = () => {
        let tabs = [];

        for(let i = 1; i <= 7; i++) {
        tabs.push(
            <Nav.Item key={i}>
            <Nav.Link eventKey={i}>Day {i}</Nav.Link>
            </Nav.Item>
        )
        }
        tabs.push(
        <Nav.Item key={8}>
            <Nav.Link eventKey="shoppinglist">Shopping List</Nav.Link>
        </Nav.Item>
        )
        tabs.push(
        <Nav.Item key={9}>
            <Nav.Link eventKey="weekoverview">Week Overview</Nav.Link>
        </Nav.Item>
        )

        return tabs;
    }

    const createTabContent = () => {
        let tabs = [];

        for(let i = 1; i <= 7; i++) {
        tabs.push(
            <Tab.Pane eventKey={i} key={i}>
                <DayView day={i} recipes={recipes} onChange={onChange} week={week} />
            </Tab.Pane>
        )
        }
        tabs.push(
        <Tab.Pane key={8} eventKey="shoppinglist">
            <ShoppingList week={week} recipes={recipes} />
        </Tab.Pane>
        )
        tabs.push(
        <Tab.Pane key={9} eventKey="weekoverview">
            <WeekOverview week={week} />
        </Tab.Pane>
        )

        return tabs;
    }

    return (
        <div className="container">
        <InputGroup size="sm" className="mb-3">
            <InputGroup.Prepend>
            <InputGroup.Text>Week</InputGroup.Text>
            </InputGroup.Prepend>
            <FormControl type="number" min="1" max="52" data-key="week" value={week.week} onChange={onChange} />
        </InputGroup>
        <Tab.Container defaultActiveKey="1">
            <Row>
            <Col sm={3}>
                <Nav variant="pills" className="flex-column">
                {createTabLinks()}
                </Nav>
            </Col>
            <Col sm={9}>
                <Tab.Content>
                {createTabContent()}
                </Tab.Content>
            </Col>
            </Row>
        </Tab.Container>
        <button className="btn btn-dark col-sm-10" onClick={() => saveWeek()}>Save Week</button>
        {week.id !== undefined ? <button className="btn btn-danger col-sm-2" onClick={() => deleteWeek(week.id)}>Delete Week</button> : ""}
        </div>
    )
}

  

function ShoppingList({ week, recipes }) {
    return (
        <Tabs defaultActiveKey="1">
        {Object.keys(week).map((key, index) => (key !== "week" && key !== "id" ?
            <Tab key={key} eventKey={key} title={"Day " + key}>
                {recipes.length > 0 ? recipes.find(r => (r.id === week[key])).ingredients.map((ingredient, index2) =>
                <ListGroup.Item key={index2}>{ingredient}</ListGroup.Item>
                ) : ""}
            </Tab>
            : "")
        )}
        </Tabs>
    )
    }

function WeekOverview({ week }) {
    return (
        <Table striped bordered hover>
        <thead>
            <tr>
            <th>Day</th>
            <th>Recipe</th>
            </tr>
        </thead>
        <tbody>
            {Object.keys(week).map(key => (key !== "week" && key !== "id" ?
            <tr key={key}>
                <td>{key}</td>
                <td>{week[key]}</td>
            </tr> : null
            ))}
        </tbody>
        </Table>
    )
}